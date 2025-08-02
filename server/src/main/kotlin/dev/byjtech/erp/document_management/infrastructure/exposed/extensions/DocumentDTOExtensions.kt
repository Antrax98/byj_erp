package dev.byjtech.erp.document_management.infrastructure.exposed.extensions

import dev.byjtech.erp.document_management.domain.model.Document
import dev.byjtech.erp.document_management.domain.model.DocumentEditHistory
import dev.byjtech.erp.document_management.domain.model.DocumentAuditLog
import dev.byjtech.erp.document_management.domain.model.DocumentNotification
import dev.byjtech.erp.document_management.domain.model.UserNotificationSettings
import dev.byjtech.erp.document_management.dto.DocumentDTO
import dev.byjtech.erp.document_management.dto.DocumentEditHistoryDTO
import dev.byjtech.erp.document_management.dto.DocumentAuditLogDTO
import dev.byjtech.erp.document_management.dto.DocumentNotificationDTO
import dev.byjtech.erp.document_management.dto.UserNotificationSettingsDTO
import dev.byjtech.erp.modules.document_management.domain.model.DocumentStatus

fun Document.toDTO(): DocumentDTO {
    return DocumentDTO(
        id = this.id.toString(),
        type = this.type,
        documentNumber = this.documentNumber,
        issueDate = this.issueDate,
        dueDate = this.dueDate,
        status = this.status, // Pasar el enum directamente
        currency = this.currency,
        netAmount = this.netAmount,
        taxAmount = this.taxAmount,
        totalAmount = this.totalAmount,
        fileUrl = this.fileUrl,
        createdBy = this.createdBy.toString(),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        active = this.active,
        companyId = this.companyId?.toString()
    )
}

fun DocumentEditHistory.toDTO(): DocumentEditHistoryDTO {
    return DocumentEditHistoryDTO(
        id = this.id.toString(),
        documentId = this.documentId.toString(),
        fieldName = this.fieldName,
        oldValue = this.oldValue,
        newValue = this.newValue,
        userId = this.userId.toString(),
        createdAt = this.createdAt
    )
}

fun DocumentAuditLog.toDTO(): DocumentAuditLogDTO {
    return DocumentAuditLogDTO(
        id = this.id.toString(),
        documentId = this.documentId.toString(),
        eventType = this.eventType.name, // Convertir enum a string
        description = this.description,
        userId = this.userId.toString(),
        createdAt = this.createdAt
    )
}

fun DocumentDTO.toDomain(): dev.byjtech.erp.document_management.domain.model.Document {
    return dev.byjtech.erp.document_management.domain.model.Document(
        id = java.util.UUID.fromString(this.id),
        type = this.type,
        documentNumber = this.documentNumber,
        issueDate = this.issueDate,
        dueDate = this.dueDate,
        status = this.status,
        currency = this.currency,
        netAmount = this.netAmount,
        taxAmount = this.taxAmount,
        totalAmount = this.totalAmount,
        fileUrl = this.fileUrl,
        createdBy = java.util.UUID.fromString(this.createdBy),
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        active = this.active,
        companyId = this.companyId?.let { java.util.UUID.fromString(it) }
    )
}

fun DocumentNotification.toDTO(): DocumentNotificationDTO {
    return DocumentNotificationDTO(
        id = this.id.toString(),
        documentId = this.documentId.toString(),
        userId = this.userId.toString(),
        notificationType = this.notificationType,
        title = this.title,
        message = this.message,
        status = this.status,
        sentAt = this.sentAt,
        createdAt = this.createdAt
    )
}

fun UserNotificationSettings.toDTO(): UserNotificationSettingsDTO {
    return UserNotificationSettingsDTO(
        id = this.id.toString(),
        userId = this.userId.toString(),
        companyId = this.companyId.toString(),
        daysBeforeExpiration = this.daysBeforeExpiration,
        emailEnabled = this.emailEnabled,
        systemEnabled = this.systemEnabled,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}
