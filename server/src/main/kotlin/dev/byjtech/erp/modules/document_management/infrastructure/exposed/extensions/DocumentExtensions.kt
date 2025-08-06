package dev.byjtech.erp.document_management.infrastructure.exposed.extensions

import dev.byjtech.erp.document_management.domain.model.Document
import dev.byjtech.erp.document_management.infrastructure.exposed.entities.DocumentEntity
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalDateTime

fun DocumentEntity.toModel(): Document {
    return Document(
        id = this.id.value,
        type = this.type,
        documentNumber = this.documentNumber,
        issueDate = this.issueDate.toKotlinLocalDate(),
        dueDate = this.dueDate?.toKotlinLocalDate(),
        status = this.status,
        currency = this.currency,
        netAmount = this.netAmount.toDouble(),
        taxAmount = this.taxAmount.toDouble(),
        totalAmount = this.totalAmount.toDouble(),
        fileUrl = this.fileUrl,
        createdBy = this.createdBy,
        createdAt = this.createdAt.toKotlinLocalDateTime(),
        updatedAt = this.updatedAt.toKotlinLocalDateTime(),
        active = this.active,
        companyId = this.companyId
    )
}
