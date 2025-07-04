package dev.byjtech.erp.document_management.infrastructure.exposed.repository

import dev.byjtech.erp.document_management.domain.model.DocumentEditHistory
import dev.byjtech.erp.document_management.domain.repository.DocumentEditHistoryRepository
import dev.byjtech.erp.document_management.infrastructure.exposed.tables.DocumentEditHistoryTable
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class DocumentEditHistoryRepositoryImpl : DocumentEditHistoryRepository {

    override fun findByDocumentId(documentId: Int): List<DocumentEditHistory> = transaction {
        DocumentEditHistoryTable
            .select { DocumentEditHistoryTable.documentId eq documentId }
            .map { it.toDomain() }
    }

    override fun save(history: DocumentEditHistory): DocumentEditHistory = transaction {
        DocumentEditHistoryTable.insert {
            it[documentId] = history.documentId
            it[fieldName] = history.fieldName
            it[oldValue] = history.oldValue
            it[newValue] = history.newValue
            it[userId] = history.userId
            it[createdAt] = history.createdAt.toJavaLocalDateTime()
        }
        history
    }

    private fun ResultRow.toDomain(): DocumentEditHistory = DocumentEditHistory(
        id = this[DocumentEditHistoryTable.id].value,
        documentId = this[DocumentEditHistoryTable.documentId].value,
        fieldName = this[DocumentEditHistoryTable.fieldName],
        oldValue = this[DocumentEditHistoryTable.oldValue],
        newValue = this[DocumentEditHistoryTable.newValue],
        userId = this[DocumentEditHistoryTable.userId].value,
        createdAt = this[DocumentEditHistoryTable.createdAt].toKotlinLocalDateTime()
    )
}
