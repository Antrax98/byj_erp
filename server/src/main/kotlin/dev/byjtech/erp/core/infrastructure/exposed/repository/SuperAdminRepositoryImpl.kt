package dev.byjtech.erp.core.infrastructure.exposed.repository

import dev.byjtech.erp.core.domain.model.SuperAdmin
import dev.byjtech.erp.core.domain.repository.SuperAdminRepository

class SuperAdminRepositoryImpl: SuperAdminRepository {
    override fun create(superAdmin: SuperAdmin): SuperAdmin {
        TODO("Not yet implemented")
    }
    override fun getByUserId(userId: Int): SuperAdmin? {
        TODO("Not yet implemented")
    }
    override fun delete(superAdminId: Int) {
        TODO("Not yet implemented")
    }
    override fun update(superAdmin: SuperAdmin): SuperAdmin {
        TODO("Not yet implemented")
    }
}