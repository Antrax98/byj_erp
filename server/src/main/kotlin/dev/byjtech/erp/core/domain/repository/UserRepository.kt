package dev.byjtech.erp.core.domain.repository

import dev.byjtech.erp.common.PermissionWithKey
import dev.byjtech.erp.core.domain.model.Permission
import dev.byjtech.erp.core.domain.model.User
import dev.byjtech.erp.core.dto.UserDTO
import java.util.UUID

interface UserRepository {
    fun create(user: UserDTO): Boolean
    fun create(user: User): User
    fun find(id: UUID): User?
    fun findWithRoles(id: UUID): User? //TODO: agregar funcion para obtener los permisos especiales del usuario
    fun findByEmail(email: String): User?
    fun update(user: User): User
    fun addRole(userId: UUID, roleId: UUID): Boolean
    fun removeRole(userId: UUID, roleId: UUID): Boolean
    fun delete(id: UUID)
    fun findByCompanyId(companyId: UUID): Set<User>
    //funciones dedicadas a permisos especiales
    fun addSpecialPermission(userId: UUID, permissionId: UUID): Boolean
    fun removeSpecialPermission(userId: UUID, permissionId: UUID): Boolean
    fun getSpecialPermissionsByUserId(userId: UUID): Set<Permission>?
}