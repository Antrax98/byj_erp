package dev.byjtech.erp.document_management.infrastructure.exposed.entities

import dev.byjtech.erp.core.infrastructure.exposed.entities.UserEntity
import dev.byjtech.erp.document_management.domain.model.Document
import dev.byjtech.erp.document_management.domain.model.DocumentStatus
import dev.byjtech.erp.modules.document_management.infrastructure.exposed.tables.DocumentsTable
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toJavaLocalDateTime
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DocumentEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DocumentEntity>(DocumentsTable)

    var documentType by DocumentsTable.documentType
    var documentNumber by DocumentsTable.documentNumber
    var issueDate by DocumentsTable.issueDate
    var dueDate by DocumentsTable.dueDate
    var status by DocumentsTable.status
    var currency by DocumentsTable.currency
    var netAmount by DocumentsTable.netAmount
    var taxAmount by DocumentsTable.taxAmount
    var totalAmount by DocumentsTable.totalAmount
    var fileUrl by DocumentsTable.fileUrl
    var createdBy by UserEntity referencedOn DocumentsTable.createdBy
    var createdAt by DocumentsTable.createdAt
    var updatedAt by DocumentsTable.updatedAt
    var active by DocumentsTable.active

    fun toDomain() = Document( //permite usar los modelos sin importar cómo se guarden
        id = id.value,
        documentType = documentType,
        documentNumber = documentNumber,
        issueDate = issueDate.toKotlinLocalDate(),
        dueDate = dueDate?.toKotlinLocalDate(),
        status = status,
        currency = currency,
        netAmount = netAmount.toDouble(),
        taxAmount = taxAmount.toDouble(),
        totalAmount = totalAmount.toDouble(),
        fileUrl = fileUrl,
        createdBy = createdBy.id.value,
        createdAt = createdAt.toKotlinLocalDateTime(),
        updatedAt = updatedAt.toKotlinLocalDateTime(),
        active = active
    )
}
