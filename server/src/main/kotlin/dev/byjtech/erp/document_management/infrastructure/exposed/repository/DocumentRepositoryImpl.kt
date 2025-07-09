package dev.byjtech.erp.document_management.infrastructure.exposed.repository

import dev.byjtech.erp.document_management.domain.model.Document
import dev.byjtech.erp.document_management.domain.model.DocumentStatus
import dev.byjtech.erp.document_management.domain.repository.DocumentRepository
import dev.byjtech.erp.document_management.infrastructure.exposed.tables.DocumentsTable
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.Database
import java.util.UUID

class DocumentRepositoryImpl(private val database: Database) : DocumentRepository {

    override fun findById(id: UUID): Document? = transaction(database) {
        DocumentsTable.select(DocumentsTable.id eq id)
            .map { it.toDomain() }
            .singleOrNull()
    }

    override fun findAll(): List<Document> = transaction(database) {
        DocumentsTable.selectAll()
            .map { it.toDomain() }
    }

    override fun save(document: Document): Document = transaction(database) {
        val insertedId = DocumentsTable.insertAndGetId { row ->
            row[documentType] = document.documentType
            row[documentNumber] = document.documentNumber
            row[issueDate] = document.issueDate.toJavaLocalDate()
            row[dueDate] = document.dueDate?.toJavaLocalDate()
            row[status] = document.status
            row[currency] = document.currency
            row[netAmount] = document.netAmount.toBigDecimal()
            row[taxAmount] = document.taxAmount.toBigDecimal()
            row[totalAmount] = document.totalAmount.toBigDecimal()
            row[fileUrl] = document.fileUrl
            row[createdBy] = document.createdBy
            row[createdAt] = document.createdAt.toJavaLocalDateTime()
            row[updatedAt] = document.updatedAt.toJavaLocalDateTime()
            row[active] = document.active
        }.value

        findById(insertedId)!!
    }

    override fun update(document: Document): Document = transaction(database) {
        DocumentsTable.update({ DocumentsTable.id eq document.id }) { row ->
            row[documentType] = document.documentType
            row[documentNumber] = document.documentNumber
            row[issueDate] = document.issueDate.toJavaLocalDate()
            row[dueDate] = document.dueDate?.toJavaLocalDate()
            row[status] = document.status
            row[currency] = document.currency
            row[netAmount] = document.netAmount.toBigDecimal()
            row[taxAmount] = document.taxAmount.toBigDecimal()
            row[totalAmount] = document.totalAmount.toBigDecimal()
            row[fileUrl] = document.fileUrl
            row[createdBy] = document.createdBy
            row[createdAt] = document.createdAt.toJavaLocalDateTime()
            row[updatedAt] = document.updatedAt.toJavaLocalDateTime()
            row[active] = document.active
        }
        findById(document.id)!!
    }

    override fun delete(id: UUID): Unit = transaction(database) {
        DocumentsTable.deleteWhere { DocumentsTable.id eq id }
    }

    private fun ResultRow.toDomain(): Document = Document(
        id = this[DocumentsTable.id].value,
        documentType = this[DocumentsTable.documentType],
        documentNumber = this[DocumentsTable.documentNumber],
        issueDate = this[DocumentsTable.issueDate].toKotlinLocalDate(),
        dueDate = this[DocumentsTable.dueDate]?.toKotlinLocalDate(),
        status = this[DocumentsTable.status],
        currency = this[DocumentsTable.currency],
        netAmount = this[DocumentsTable.netAmount].toDouble(),
        taxAmount = this[DocumentsTable.taxAmount].toDouble(),
        totalAmount = this[DocumentsTable.totalAmount].toDouble(),
        fileUrl = this[DocumentsTable.fileUrl],
        createdBy = this[DocumentsTable.createdBy].value,
        createdAt = this[DocumentsTable.createdAt].toKotlinLocalDateTime(),
        updatedAt = this[DocumentsTable.updatedAt].toKotlinLocalDateTime(),
        active = this[DocumentsTable.active],
        companyId = null, // TODO: Add company reference when needed
        categoryId = null // TODO: Add category reference when needed
    )
}