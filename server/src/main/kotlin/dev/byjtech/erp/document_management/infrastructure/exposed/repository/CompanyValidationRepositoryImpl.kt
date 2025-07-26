package dev.byjtech.erp.document_management.infrastructure.exposed.repository

import dev.byjtech.erp.document_management.domain.repository.CompanyValidationRepository
import dev.byjtech.erp.core.infrastructure.exposed.entities.CompanyEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.UserEntity
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

/**
 * Implementación del repositorio de validación que accede a la base de datos core
 * para validar referencias cruzadas desde el módulo document_management
 */
class CompanyValidationRepositoryImpl(private val coreDatabase: Database) : CompanyValidationRepository {
    
    override fun existsById(companyId: UUID): Boolean {
        return transaction(coreDatabase) {
            CompanyEntity.findById(companyId) != null
        }
    }
    
    override fun userExistsById(userId: UUID): Boolean {
        return transaction(coreDatabase) {
            UserEntity.findById(userId) != null
        }
    }
}
