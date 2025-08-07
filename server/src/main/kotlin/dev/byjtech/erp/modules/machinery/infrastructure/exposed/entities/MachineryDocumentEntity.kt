package dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities

import dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MachineryDocumentsTable
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class MachineryDocumentEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<MachineryDocumentEntity>(dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MachineryDocumentsTable)

    var machinery by MachineryEntity referencedOn dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MachineryDocumentsTable.machineryId
    var documentType by dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MachineryDocumentsTable.documentType
    var description by dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MachineryDocumentsTable.description
    var fileName by dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MachineryDocumentsTable.fileName
    var filePath by dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MachineryDocumentsTable.filePath
    var issueDate by dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MachineryDocumentsTable.issueDate
    var expirationDate by dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MachineryDocumentsTable.expirationDate
    var uploadedBy by dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MachineryDocumentsTable.uploadedBy
    var createdAt by dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MachineryDocumentsTable.createdAt
    var updatedAt by dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MachineryDocumentsTable.updatedAt
    var isActive by dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MachineryDocumentsTable.isActive
}
