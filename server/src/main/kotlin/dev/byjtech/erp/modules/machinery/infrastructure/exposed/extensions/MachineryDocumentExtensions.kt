package dev.byjtech.erp.modules.machinery.infrastructure.exposed.extensions

import dev.byjtech.erp.modules.machinery.domain.model.MachineryDocument
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities.MachineryDocumentEntity
import dev.byjtech.erp.utils.datetime.toKotlinx
import kotlinx.datetime.toJavaLocalDateTime

fun MachineryDocumentEntity.toModel(): MachineryDocument {
    return MachineryDocument(
        id = this.id.value,
        machineryId = this.machinery.id.value,
        documentType = this.documentType,
        description = this.description,
        fileName = this.fileName,
        filePath = this.filePath,
        issueDate = this.issueDate?.toKotlinx(),
        expirationDate = this.expirationDate?.toKotlinx(),
        uploadedBy = this.uploadedBy,
        createdAt = this.createdAt.toKotlinx(),
        updatedAt = this.updatedAt.toKotlinx(),
        isActive = this.isActive
    )
}

fun MachineryDocumentEntity.fromModel(document: MachineryDocument) {
    this.documentType = document.documentType
    this.description = document.description
    this.fileName = document.fileName
    this.filePath = document.filePath
    this.issueDate = document.issueDate?.toJavaLocalDateTime()
    this.expirationDate = document.expirationDate?.toJavaLocalDateTime()
    this.uploadedBy = document.uploadedBy
    this.createdAt = document.createdAt.toJavaLocalDateTime()
    this.updatedAt = document.updatedAt.toJavaLocalDateTime()
    this.isActive = document.isActive
}
