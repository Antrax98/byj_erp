package dev.byjtech.erp.core.infrastructure.exposed.repository

import dev.byjtech.erp.core.domain.model.Permission
import dev.byjtech.erp.core.domain.model.User
import dev.byjtech.erp.core.domain.repository.UserRepository
import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.CompanyEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.PermissionEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.RoleEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.UserEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.UserPermissionEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.UserRoleEntity
import dev.byjtech.erp.core.infrastructure.exposed.extensions.fromModel
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toModel
import dev.byjtech.erp.core.infrastructure.exposed.tables.UserPermissionTable
import dev.byjtech.erp.core.infrastructure.exposed.tables.UserRoleTable
import dev.byjtech.erp.core.infrastructure.exposed.tables.UsersTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime

class UserRepositoryImpl: UserRepository {
    override fun create(user: UserDTO): Boolean {
        return transaction {
            try {
                UserEntity.new {
                    name = user.name
                    email = user.email
                    isActive = true
                    pictureUrl = user.pictureUrl
                    company = user.companyId?.let { CompanyEntity[it] }
                    createdAt = LocalDateTime.now()
                    updatedAt = LocalDateTime.now()
                }
            } catch (e: Exception) {
                println("Error creating user: ${e.message}")
                return@transaction false
            }
            return@transaction true
        }
    }

    override fun create(user: User): User{
        return transaction {
            try {
                val userEntity = UserEntity.new {
                    name = user.name
                    email = user.email
                    isActive = true
                    company = user.companyId?.let { CompanyEntity[it] }
                    createdAt = LocalDateTime.now()
                    updatedAt = LocalDateTime.now()
                }
                return@transaction userEntity.toModel()
            } catch (e: Exception) {
                println("Error creating user: ${e.message}")
                throw e
            }
        }
    }

    override fun find(id: Int): User? {
        return transaction {
            UserEntity.findById(id)?.toModel()
        }
    }

    override fun findWithRoles(id: Int): User? {
        return transaction {
            val userRoles = UserRoleEntity.find { UserRoleTable.userId eq id }.mapNotNull { RoleEntity.findById(it.role.id) }
            val userModel = UserEntity.findById(id)?.toModel(userRoles.toSet())
            return@transaction userModel
        }
    }

    override fun findByEmail(email: String): User? {
        TODO("Not yet implemented")
    }

    //no usar para hacer update a los roles del user, usar el metodo update del repository de roles
    //solo hase update al user, no a su relacion con roles
    override fun update(user: User): User {
        return transaction {
            val userEntity = UserEntity.findById(user.id) ?: throw Exception("User not found")
            userEntity.fromModel(user)
            return@transaction userEntity.toModel()
        }
    }

    // si el usuario ya tiene el rol, retorna false
    override fun addRole(userId: Int, roleId: Int): Boolean {
        return transaction {
            val exist = UserRoleEntity.find { (UserRoleTable.userId eq userId) and (UserRoleTable.roleId eq roleId) }.firstOrNull()
            if(exist != null){
                return@transaction false
            } else {
                UserRoleEntity.new {
                    this.user = UserEntity[userId]
                    this.role = RoleEntity[roleId]
                }
                return@transaction true
            }
        }
    }

    // si el usuario no tenia el rol, retorna false
    override fun removeRole(userId: Int, roleId: Int): Boolean {
        return transaction {
            val exist = UserRoleEntity.find { (UserRoleTable.userId eq userId) and (UserRoleTable.roleId eq roleId) }.firstOrNull()
            if(exist == null){
                return@transaction false
            } else {
                exist.delete()
                return@transaction true
            }
        }
    }

    //mejor si no se usa
    override fun delete(id: Int) {
        transaction {
            UserEntity[id].delete()
        }
    }
    override fun findByCompanyId(companyId: Int): Set<User> {
        return transaction {
            UserEntity.find { UsersTable.companyId eq companyId }.map { it.toModel() }.toSet()
        }
    }

    //funciones dedicadas a permisos especiales

    override fun addSpecialPermission(userId: Int, permissionId: Int): Boolean {
        return transaction {
            //chekear si existe el usuario y el permiso, por ahora solo retornan false de no existir
            val user = UserEntity.findById(userId)
            val permission = PermissionEntity.findById(permissionId)
            if (user == null || permission == null) {
                return@transaction false
            }

            val exist = UserPermissionEntity.find { (UserPermissionTable.userId eq userId) and (UserPermissionTable.permissionId eq permissionId) }.firstOrNull()
            if (exist != null) {
                return@transaction false
            } else {
                UserPermissionEntity.new {
                    this.user = UserEntity[userId]
                    this.permission = PermissionEntity[permissionId]
                }
            }
            return@transaction true
        }
    }
    override fun removeSpecialPermission(userId: Int, permissionId: Int): Boolean {
        return transaction {
            //chekear si existe el usuario y el permiso, por ahora solo retornan false de no existir
            val user = UserEntity.findById(userId)
            val permission = PermissionEntity.findById(permissionId)
            if (user == null || permission == null) {
                return@transaction false
            }

            val exist = UserPermissionEntity.find { (UserPermissionTable.userId eq userId) and (UserPermissionTable.permissionId eq permissionId) }.firstOrNull()
            if (exist == null) {
                return@transaction false
            } else {
                exist.delete()
                return@transaction true
            }

        }
    }
    override fun getSpecialPermissionsByUserId(userId: Int): Set<Permission>? {
        return transaction {
            UserEntity.findById(userId) ?: return@transaction null
            val permissions = UserPermissionEntity.find { UserPermissionTable.userId eq userId }.map { it.permission }
            return@transaction permissions.map { it.toModel() }.toSet()
        }
    }
}