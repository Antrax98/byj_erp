package dev.byjtech.erp.machinery.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object MachineryDocumentsTable : UUIDTable("machinery_documents") {
    val machineryId = reference("machinery_id", MachineriesTable)
    val documentType = varchar("document_type", 50)
    val description = text("description")
    val fileName = varchar("file_name", 255)
    val filePath = varchar("file_path", 255)
    val issueDate = datetime("issue_date").nullable()
    val expirationDate = datetime("expiration_date").nullable()
    val uploadedBy = uuid("uploaded_by")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val isActive = bool("is_active").default(true)
}
