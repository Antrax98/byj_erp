package dev.byjtech.erp.modules.document_management.infrastructure.exposed.extensions

import dev.byjtech.erp.document_management.domain.model.DocumentEditHistory
import dev.byjtech.erp.document_management.dto.DocumentEditHistoryDTO
import dev.byjtech.erp.document_management.infrastructure.exposed.entities.DocumentEditHistoryEntity
import dev.byjtech.erp.utils.datetime.toKotlinx
import kotlinx.datetime.toKotlinLocalDateTime

fun DocumentEditHistoryEntity.toDTO(): DocumentEditHistoryDTO {
    return DocumentEditHistoryDTO(
        id = this.id.value.toString(),
        documentId = this.document.id.value.toString(),
        fieldName = this.fieldName,
        oldValue = this.oldValue,
        newValue = this.newValue,
        userId = this.user.id.value.toString(),
        createdAt = this.createdAt.toKotlinLocalDateTime()
    )
}

fun DocumentEditHistoryEntity.toModel(): DocumentEditHistory {
    return DocumentEditHistory(
        id = this.id.value,
        documentId = this.document.id.value,
        fieldName = this.fieldName,
        oldValue = this.oldValue,
        newValue = this.newValue,
        userId = this.user.id.value,
        createdAt = this.createdAt.toKotlinLocalDateTime()
    )
}
