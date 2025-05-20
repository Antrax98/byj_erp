package dev.byjtech.erp.core.infrastructure.exposed.repository

import dev.byjtech.erp.config.UserInfo
import dev.byjtech.erp.core.domain.model.User
import dev.byjtech.erp.core.domain.repository.UserRepository
import dev.byjtech.erp.core.infrastructure.exposed.entities.UserEntity
import dev.byjtech.erp.core.infrastructure.exposed.extensions.fromDomain
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toDomain
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepositoryImpl: UserRepository {
    override fun create(user: User): User {
        TODO("Not yet implemented")
    }
    override fun getById(id: Int): User? {
        TODO("Not yet implemented")
    }
    override fun getByEmail(email: String): User? {
        TODO("Not yet implemented")
    }
    override fun update(user: User): User {
        return transaction {
            val userEntity = UserEntity.findById(user.id) ?: throw Exception("User not found")
            userEntity.fromDomain(user)
            return@transaction userEntity.toDomain()
        }
    }
    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }
    override fun findByCompanyId(companyId: Int): Set<User> {
        TODO("Not yet implemented")
    }
}