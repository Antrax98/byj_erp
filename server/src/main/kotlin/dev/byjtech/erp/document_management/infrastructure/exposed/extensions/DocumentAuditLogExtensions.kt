package dev.byjtech.erp.modules.document_management.infrastructure.exposed.extensions

import dev.byjtech.erp.document_management.domain.model.DocumentAuditLog
import dev.byjtech.erp.document_management.dto.DocumentAuditLogDTO
import dev.byjtech.erp.document_management.infrastructure.exposed.entities.DocumentAuditLogEntity
import dev.byjtech.erp.utils.datetime.toKotlinx
import kotlinx.datetime.toKotlinLocalDateTime

fun DocumentAuditLogEntity.toDTO(): DocumentAuditLogDTO {
    return DocumentAuditLogDTO(
        id = this.id.value.toString(),
        documentId = this.document.id.value.toString(),
        eventType = this.eventType.name,
        description = this.description,
        userId = this.user.id.value.toString(),
        createdAt = this.createdAt.toKotlinLocalDateTime()
    )
}

fun DocumentAuditLogEntity.toModel(): DocumentAuditLog {
    return DocumentAuditLog(
        id = this.id.value,
        documentId = this.document.id.value,
        eventType = this.eventType,
        description = this.description,
        userId = this.user.id.value,
        createdAt = this.createdAt.toKotlinLocalDateTime()
    )
}
