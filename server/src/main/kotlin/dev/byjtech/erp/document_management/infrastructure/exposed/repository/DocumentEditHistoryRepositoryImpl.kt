package dev.byjtech.erp.document_management.infrastructure.exposed.repository

import dev.byjtech.erp.core.infrastructure.exposed.entities.UserEntity
import dev.byjtech.erp.document_management.domain.model.DocumentEditHistory
import dev.byjtech.erp.document_management.domain.repository.DocumentEditHistoryRepository
import dev.byjtech.erp.document_management.infrastructure.exposed.entities.DocumentEditHistoryEntity
import dev.byjtech.erp.document_management.infrastructure.exposed.entities.DocumentEntity
import dev.byjtech.erp.document_management.infrastructure.exposed.tables.DocumentEditHistoryTable
import dev.byjtech.erp.document_management.infrastructure.exposed.tables.DocumentsTable
import kotlinx.datetime.toJavaLocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class DocumentEditHistoryRepositoryImpl(private val database: Database) : DocumentEditHistoryRepository {

    override fun findByDocumentId(documentId: UUID): List<DocumentEditHistory> = transaction(database) {
        DocumentEditHistoryEntity.find { DocumentEditHistoryTable.documentId eq documentId }
            .map { it.toDomain() }
    }

    override fun findByCompanyId(companyId: UUID): List<DocumentEditHistory> = transaction(database) {
        val documentIds = DocumentsTable.selectAll().where { DocumentsTable.companyId eq companyId }
            .map { it[DocumentsTable.id] }
        
        DocumentEditHistoryEntity.find { 
            DocumentEditHistoryTable.documentId inList documentIds
        }.orderBy(DocumentEditHistoryTable.createdAt to SortOrder.DESC)
            .map { it.toDomain() }
    }

    override fun save(history: DocumentEditHistory): DocumentEditHistory = transaction(database) {
        val entity = DocumentEditHistoryEntity.new {
            document = DocumentEntity[history.documentId]
            fieldName = history.fieldName
            oldValue = history.oldValue
            newValue = history.newValue
            user = history.userId
            createdAt = history.createdAt.toJavaLocalDateTime()
        }
        entity.toDomain()
    }
}
