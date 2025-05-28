package dev.byjtech.erp.core.infrastructure.exposed.repository

import dev.byjtech.erp.core.domain.model.User
import dev.byjtech.erp.core.domain.repository.UserRepository
import dev.byjtech.erp.core.infrastructure.exposed.entities.RoleEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.UserEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.UserRoleEntity
import dev.byjtech.erp.core.infrastructure.exposed.extensions.fromModel
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toModel
import dev.byjtech.erp.core.infrastructure.exposed.tables.UserRoleTable
import dev.byjtech.erp.core.infrastructure.exposed.tables.UsersTable
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepositoryImpl: UserRepository {
    override fun create(user: User): User {
        TODO("Not yet implemented")
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
}