package dev.byjtech.erp.core.domain.repository

import dev.byjtech.erp.core.domain.model.Permission
import dev.byjtech.erp.core.domain.model.User
import dev.byjtech.erp.core.dto.UserDTO

interface UserRepository {
    fun create(user: UserDTO): Boolean
    fun create(user: User): User
    fun find(id: Int): User?
    fun findWithRoles(id: Int): User? //TODO: agregar funcion para obtener los permisos especiales del usuario
    fun findByEmail(email: String): User?
    fun update(user: User): User
    fun addRole(userId: Int, roleId: Int): Boolean
    fun removeRole(userId: Int, roleId: Int): Boolean
    fun delete(id: Int)
    fun findByCompanyId(companyId: Int): Set<User>
    //funciones dedicadas a permisos especiales
    fun addSpecialPermission(userId: Int, permissionId: Int): Boolean
    fun removeSpecialPermission(userId: Int, permissionId: Int): Boolean
    fun getSpecialPermissionsByUserId(userId: Int): Set<Permission>?
}