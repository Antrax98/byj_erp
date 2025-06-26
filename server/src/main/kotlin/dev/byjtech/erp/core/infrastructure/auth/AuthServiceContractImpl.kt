package dev.byjtech.erp.core.infrastructure.auth

import dev.byjtech.erp.shared.contracts.core.auth.AuthServiceContract
import dev.byjtech.erp.shared.contracts.core.auth.SessionNotFoundException
import dev.byjtech.erp.shared.contracts.core.auth.ValidatedSessionInfo
import dev.byjtech.erp.core.domain.repository.SessionRepository
import dev.byjtech.erp.core.domain.repository.SubscriptionRepository
import dev.byjtech.erp.core.domain.repository.SuperAdminRepository
import dev.byjtech.erp.core.domain.repository.UserRepository
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.config.logger
import dev.byjtech.erp.core.domain.model.Permission
import dev.byjtech.erp.core.domain.repository.ModuleRepository
import dev.byjtech.erp.core.domain.repository.RoleRepository
import dev.byjtech.erp.core.session.AppSession
import dev.byjtech.erp.shared.contracts.core.auth.MissingPermissionsException
import dev.byjtech.erp.shared.contracts.core.auth.MissingSuperAdminException
import dev.byjtech.erp.shared.contracts.core.auth.ModuleAccessException
import io.ktor.server.application.ApplicationCall
import java.util.UUID

//este se podria crear como un wrapper de la clase AuthService, destiando a solo servir el service interno a los demas

//TODO: quitar los repo y hacer que solo use un Application/service el cual si tendra los repository
// y las funciones justas para este caso
class AuthServiceContractImpl(
    private val userRepo: UserRepository,
    private val sessionRepo: SessionRepository,
    private val roleRepo: RoleRepository,
    private val moduleRepo: ModuleRepository,
    private val superAdminRepo: SuperAdminRepository,
    private val subscriptionRepo: SubscriptionRepository
): AuthServiceContract {
    override fun validateSessionAndAuthorize(
        call: ApplicationCall,
        module: String,
        requiredAnyPermissions: Set<PermissionKey>?,
        requiredSuperAdmin: Boolean
    ): ValidatedSessionInfo {

        if(requiredAnyPermissions != null && requiredSuperAdmin){
            throw IllegalArgumentException("Cannot set both requiredAnyPermissions and requiredSuperAdmin")
        }

//        if (requiredAnyPermissions == null && !requiredSuperAdmin){
//            //POR AHORA LO DEJA PASAR
//            //esto aria qeu no necesite permisos, cosa de solo recuperar la session
//        }
        val authHeader = call.request.headers["Authentication"]
        val token = authHeader?.removePrefix("Bearer ")?.trim()
        logger.debug("Token: {}", token)
        val appSession = token?.let { AppSession.fromEncoded(it) }
        if(appSession == null){
            throw IllegalStateException("AppSession is null")
        }

        val session = sessionRepo.find(UUID.fromString(appSession.sessionId))
        if(session == null){
            throw SessionNotFoundException(appSession.sessionId.toString())
        }

        val user = userRepo.find(session.userId)
        if(user == null){
            throw IllegalStateException("User is null")
        }

        if(requiredAnyPermissions != null){
            if(user.companyId == null){
                throw IllegalStateException("User has no company")
            }
            //1. chequear si la empresa tiene acceso al modulo y si esta habilitado

            val subscription = subscriptionRepo.findByCompanyIdAndModule(user.companyId, module)
            if(subscription == null){
                //TODO: crear un exception especifico para cuando no tiene una suscripcion
                throw ModuleAccessException(module)
            }
            if(!subscription.isActive) {
                //TODO: crear un exception especifico para cuando no esta activado por el tenant admin
                throw ModuleAccessException(module)
            }
            if(!subscription.isAccessible){
                //TODO: crear un exception especifico para cuando el modulo fue desactivado por el SuperAdmin
                throw ModuleAccessException(module)
            }

            //2. chequear si el usuario tiene los permisos requeridos
            val userRoles = roleRepo.findByUserId(user.id)
            var userPermissions: Set<Permission> = userRoles
                .flatMap { role -> roleRepo.getPermissionsByRoleId(role.id) }
                .toSet()
            val userSpecialPermissions = userRepo.getSpecialPermissionsByUserId(user.id)?: emptySet()
            userPermissions = userPermissions + userSpecialPermissions
            val requiredPermissions: Set<Permission> = moduleRepo.findPermissionsByPermissionKeySet(requiredAnyPermissions)
            val pass: Boolean = userPermissions.any { it in requiredPermissions }

            //si requiredAnyPermissions se deja vacio o null entonses se dejara pasar como sea
            if(!pass && requiredAnyPermissions.isNotEmpty()){
                throw MissingPermissionsException(requiredAnyPermissions.map { it.toString() })
            }

        }

        if(requiredSuperAdmin){
            superAdminRepo.getByUserId(user.id) ?: throw MissingSuperAdminException()
        }

        return ValidatedSessionInfo(user.id, session.id, user.companyId) //TODO() darle mas valores necesarorios a este objeto
        //por ahora userid y sessionid parecen ser los unicos necesarios, se podria agregar el company nullable


    }

}