package dev.byjtech.erp.core.domain.repository

import dev.byjtech.erp.core.domain.model.User

interface UserRepository {
    fun create(user: User): User
    fun find(id: Int): User?
    fun findWithRoles(id: Int): User?
    fun findByEmail(email: String): User?
    fun update(user: User): User
    fun addRole(userId: Int, roleId: Int): Boolean
    fun removeRole(userId: Int, roleId: Int): Boolean
    fun delete(id: Int)
    fun findByCompanyId(companyId: Int): Set<User>
}