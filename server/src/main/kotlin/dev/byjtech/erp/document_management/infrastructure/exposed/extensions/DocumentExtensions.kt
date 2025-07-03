package dev.byjtech.erp.modules.document_management.infrastructure.exposed.extensions

import dev.byjtech.erp.modules.document_management.domain.model.Document
import dev.byjtech.erp.modules.document_management.dto.DocumentDTO
import dev.byjtech.erp.modules.document_management.infrastructure.exposed.entities.DocumentEntity
import dev.byjtech.erp.utils.datetime.toKotlinx
import kotlinx.datetime.toKotlinLocalDate
import kotlinx.datetime.toKotlinLocalDateTime

fun DocumentEntity.toDTO(): DocumentDTO {
    return DocumentDTO(
        id = this.id.value.toString(),
        documentType = this.documentType,
        documentNumber = this.documentNumber,
        issueDate = this.issueDate.toKotlinLocalDate(),
        dueDate = this.dueDate?.toKotlinLocalDate(),
        status = this.status.name,
        currency = this.currency,
        netAmount = this.netAmount.toDouble(),
        taxAmount = this.taxAmount.toDouble(),
        totalAmount = this.totalAmount.toDouble(),
        fileUrl = this.fileUrl,
        createdBy = this.createdBy.id.value.toString(),
        createdAt = this.createdAt.toKotlinLocalDateTime(),
        updatedAt = this.updatedAt.toKotlinLocalDateTime(),
        active = this.active
    )
}

fun DocumentEntity.toModel(): Document {
    return Document(
        id = this.id.value,
        documentType = this.documentType,
        documentNumber = this.documentNumber,
        issueDate = this.issueDate.toKotlinLocalDate(),
        dueDate = this.dueDate?.toKotlinLocalDate(),
        status = this.status,
        currency = this.currency,
        netAmount = this.netAmount.toDouble(),
        taxAmount = this.taxAmount.toDouble(),
        totalAmount = this.totalAmount.toDouble(),
        fileUrl = this.fileUrl,
        createdBy = this.createdBy.id.value,
        createdAt = this.createdAt.toKotlinLocalDateTime(),
        updatedAt = this.updatedAt.toKotlinLocalDateTime(),
        active = this.active
    )
}
