package dev.byjtech.erp.machinery.infrastructure.exposed.entities

import dev.byjtech.erp.machinery.infrastructure.exposed.tables.MachineryDocumentsTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class MachineryDocumentEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<MachineryDocumentEntity>(MachineryDocumentsTable)

    var machinery by MachineryEntity referencedOn MachineryDocumentsTable.machineryId
    var documentType by MachineryDocumentsTable.documentType
    var description by MachineryDocumentsTable.description
    var fileName by MachineryDocumentsTable.fileName
    var filePath by MachineryDocumentsTable.filePath
    var issueDate by MachineryDocumentsTable.issueDate
    var expirationDate by MachineryDocumentsTable.expirationDate
    var uploadedBy by MachineryDocumentsTable.uploadedBy
    var createdAt by MachineryDocumentsTable.createdAt
    var updatedAt by MachineryDocumentsTable.updatedAt
    var isActive by MachineryDocumentsTable.isActive
}
