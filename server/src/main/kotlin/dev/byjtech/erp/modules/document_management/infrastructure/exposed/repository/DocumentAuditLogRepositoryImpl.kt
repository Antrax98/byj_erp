package dev.byjtech.erp.document_management.infrastructure.exposed.repository

import dev.byjtech.erp.core.infrastructure.exposed.entities.UserEntity
import dev.byjtech.erp.document_management.domain.model.DocumentAuditLog
import dev.byjtech.erp.document_management.domain.repository.DocumentAuditLogRepository
import dev.byjtech.erp.document_management.infrastructure.exposed.entities.DocumentAuditLogEntity
import dev.byjtech.erp.document_management.infrastructure.exposed.entities.DocumentEntity
import dev.byjtech.erp.document_management.infrastructure.exposed.tables.DocumentAuditLogTable
import dev.byjtech.erp.document_management.infrastructure.exposed.tables.DocumentsTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import kotlinx.datetime.toJavaLocalDateTime

class DocumentAuditLogRepositoryImpl(
    private val db: Database
) : DocumentAuditLogRepository {

    override fun create(log: DocumentAuditLog): DocumentAuditLog = transaction(db) {
        val entity = DocumentAuditLogEntity.new {
            document = DocumentEntity[log.documentId]
            eventType = log.eventType
            description = log.description
            user = log.userId
            createdAt = log.createdAt.toJavaLocalDateTime()
        }
        entity.toDomain()
    }

    override fun findByDocumentId(documentId: UUID): List<DocumentAuditLog> = transaction(db) {
        DocumentAuditLogEntity.find { DocumentAuditLogTable.documentId eq documentId }
            .map { it.toDomain() }
    }

    override fun findByCompanyId(companyId: UUID): List<DocumentAuditLog> = transaction(db) {
        val documentIds = DocumentsTable.selectAll().where { DocumentsTable.companyId eq companyId }
            .map { it[DocumentsTable.id] }
        
        DocumentAuditLogEntity.find { 
            DocumentAuditLogTable.documentId inList documentIds
        }.orderBy(DocumentAuditLogTable.createdAt to SortOrder.DESC)
            .map { it.toDomain() }
    }

    override fun deleteById(id: UUID): Boolean = transaction(db) {
        val entity = DocumentAuditLogEntity.findById(id) ?: return@transaction false
        entity.delete()
        true
    }
}
