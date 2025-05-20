package dev.byjtech.erp.core.infrastructure.auth

import dev.byjtech.erp.shared.contracts.core.auth.AuthServiceContract
import dev.byjtech.erp.shared.contracts.core.auth.SessionNotFoundException
import dev.byjtech.erp.shared.contracts.core.auth.ValidatedSessionInfo
import dev.byjtech.erp.core.domain.repository.CompanyRepository
import dev.byjtech.erp.core.domain.repository.SessionRepository
import dev.byjtech.erp.core.domain.repository.SubscriptionRepository
import dev.byjtech.erp.core.domain.repository.SuperAdminRepository
import dev.byjtech.erp.core.domain.repository.UserRepository
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.core.session.AppSession

//este se podria crear como un wrapper de la clase AuthService, destiando a solo servir el service interno a los demas

//TODO: quitar los repo y hacer que solo use un Application/service el cual si tendra los repository
// y las funciones justas para este caso
class AuthServiceContractImpl(
    private val userRepo: UserRepository,
    private val sessionRepo: SessionRepository,
    private val companyRepo: CompanyRepository,
    private val superAdminRepo: SuperAdminRepository,
    private val subscriptionRepo: SubscriptionRepository
): AuthServiceContract {
    override fun validateSessionAndAuthorize(
        authHeader: String?,
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

        val token = authHeader?.removePrefix("Bearer ")?.trim()
        val appSession = token?.let { AppSession.fromEncoded(it) }
        if(appSession == null){
            throw IllegalStateException("AppSession is null")
        }

        val session = sessionRepo.find(appSession.sessionId)
        if(session == null){
            throw SessionNotFoundException(appSession.sessionId.toString())
        }

        val user = userRepo.getById(session.user.id)
        if(user == null){
            throw IllegalStateException("User is null")
        }

        if(requiredAnyPermissions != null){
            //TODO() chequear si la empresa tiene acceso al modulo,si el modulo esta habilitado y si el usuario tiene los permisos requeridos
        }

        if(requiredSuperAdmin){
            //TODO() chequear si es superadmin y lo necesario de SuperAdmin
        }

        return ValidatedSessionInfo(user.id, session.id) //TODO() darle mas valores necesarorios a este objeto



    }

}