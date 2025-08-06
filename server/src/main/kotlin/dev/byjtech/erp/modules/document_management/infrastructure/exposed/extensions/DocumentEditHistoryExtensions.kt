package dev.byjtech.erp.document_management.infrastructure.exposed.extensions

import dev.byjtech.erp.document_management.domain.model.DocumentEditHistory
import dev.byjtech.erp.document_management.infrastructure.exposed.entities.DocumentEditHistoryEntity
import kotlinx.datetime.toKotlinLocalDateTime

fun DocumentEditHistoryEntity.toModel(): DocumentEditHistory {
    return DocumentEditHistory(
        id = this.id.value,
        documentId = this.document.id.value,
        fieldName = this.fieldName,
        oldValue = this.oldValue,
        newValue = this.newValue,
        userId = this.user,
        createdAt = this.createdAt.toKotlinLocalDateTime()
    )
}
