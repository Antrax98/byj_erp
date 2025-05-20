package dev.byjtech.erp.core.domain.repository

import dev.byjtech.erp.config.UserInfo //TODO() moverlo hasta una parte que solo core lo contenga
import dev.byjtech.erp.core.domain.model.User

interface UserRepository {
    fun create(user: User): User
    fun getById(id: Int): User?
    fun getByEmail(email: String): User?
    fun update(user: User): User
    fun delete(id: Int)
    fun findByCompanyId(companyId: Int): Set<User>
}