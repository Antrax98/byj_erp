package dev.byjtech.erp.machinery.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

data class MachineryDocument(
    val id: UUID = UUID.randomUUID(),
    val machineryId: UUID,
    val documentType: String,
    val description: String,
    val fileName: String,
    val filePath: String,
    val issueDate: LocalDateTime? = null,
    val expirationDate: LocalDateTime? = null,
    val uploadedBy: UUID,
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val updatedAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val isActive: Boolean = true
) {
    fun updateDescription(newDescription: String) = copy(description = newDescription)
    fun updateDocumentType(newDocumentType: String) = copy(documentType = newDocumentType)
    fun updateFileName(newFileName: String) = copy(fileName = newFileName)
    fun updateFilePath(newFilePath: String) = copy(filePath = newFilePath)
    fun updateIssueDate(newIssueDate: LocalDateTime?) = copy(issueDate = newIssueDate)
    fun updateExpirationDate(newExpirationDate: LocalDateTime?) = copy(expirationDate = newExpirationDate)
    fun activate() = copy(isActive = true)
    fun deactivate() = copy(isActive = false)
    
    fun isExpired(): Boolean {
        return expirationDate?.let { 
            it < Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        } ?: false
    }
}
