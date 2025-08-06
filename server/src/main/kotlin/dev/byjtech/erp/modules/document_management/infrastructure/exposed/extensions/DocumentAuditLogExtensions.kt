package dev.byjtech.erp.document_management.infrastructure.exposed.extensions

import dev.byjtech.erp.document_management.domain.model.DocumentAuditLog
import dev.byjtech.erp.document_management.infrastructure.exposed.entities.DocumentAuditLogEntity
import kotlinx.datetime.toKotlinLocalDateTime

fun DocumentAuditLogEntity.toModel(): DocumentAuditLog {
    return DocumentAuditLog(
        id = this.id.value,
        documentId = this.document.id.value,
        eventType = this.eventType,
        description = this.description,
        userId = this.user,
        createdAt = this.createdAt.toKotlinLocalDateTime()
    )
}
