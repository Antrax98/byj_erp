package dev.byjtech.erp.document_management.infrastructure.exposed.entities

import dev.byjtech.erp.core.infrastructure.exposed.entities.UserEntity
import dev.byjtech.erp.document_management.domain.model.DocumentAuditLog
import dev.byjtech.erp.document_management.domain.model.DocumentEventType
import dev.byjtech.erp.document_management.infrastructure.exposed.tables.DocumentAuditLogTable
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class DocumentAuditLogEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<DocumentAuditLogEntity>(DocumentAuditLogTable)

    var document by DocumentEntity referencedOn DocumentAuditLogTable.documentId
    var eventType by DocumentAuditLogTable.eventType
    var description by DocumentAuditLogTable.description
    var user by DocumentAuditLogTable.userId
    var createdAt by DocumentAuditLogTable.createdAt

    fun toDomain() = DocumentAuditLog( //permite usar los modelos sin importar cómo se guarden
        id = id.value,
        documentId = document.id.value,
        eventType = eventType,
        description = description,
        userId = user,
        createdAt = createdAt.toKotlinLocalDateTime()
    )
}
