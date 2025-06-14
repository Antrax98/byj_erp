package dev.byjtech.erp.core.infrastructure.exposed.repository

import dev.byjtech.erp.core.domain.model.SuperAdmin
import dev.byjtech.erp.core.domain.repository.SuperAdminRepository
import dev.byjtech.erp.core.infrastructure.exposed.entities.SuperAdminEntity
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toModel
import dev.byjtech.erp.core.infrastructure.exposed.tables.SuperAdminsTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

class SuperAdminRepositoryImpl(private val db: Database): SuperAdminRepository {
    override fun create(superAdmin: SuperAdmin): SuperAdmin {
        TODO("Not yet implemented")
    }
    override fun getByUserId(userId: Int): SuperAdmin? {
        return transaction(db) {
            val superAdmin = SuperAdminEntity.find { SuperAdminsTable.userId eq userId }.firstOrNull()

            return@transaction superAdmin?.toModel()
        }
    }
    override fun delete(superAdminId: Int) {
        TODO("Not yet implemented")
    }
    override fun update(superAdmin: SuperAdmin): SuperAdmin {
        TODO("Not yet implemented")
    }
}