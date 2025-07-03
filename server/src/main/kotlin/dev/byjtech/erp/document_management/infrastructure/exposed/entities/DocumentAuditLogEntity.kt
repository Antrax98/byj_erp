package dev.byjtech.erp.modules.document_management.infrastructure.exposed.entities

import dev.byjtech.erp.core.infrastructure.exposed.entities.UserEntity
import dev.byjtech.erp.modules.document_management.domain.model.DocumentAuditLog
import dev.byjtech.erp.modules.document_management.domain.model.DocumentEventType
import dev.byjtech.erp.modules.document_management.infrastructure.exposed.tables.DocumentAuditLogTable
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DocumentAuditLogEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DocumentAuditLogEntity>(DocumentAuditLogTable)

    var document by DocumentEntity referencedOn DocumentAuditLogTable.documentId
    var eventType by DocumentAuditLogTable.eventType
    var description by DocumentAuditLogTable.description
    var user by UserEntity referencedOn DocumentAuditLogTable.userId
    var createdAt by DocumentAuditLogTable.createdAt

    fun toDomain() = DocumentAuditLog( //permite usar los modelos sin importar cómo se guarden
        id = id.value,
        documentId = document.id.value,
        eventType = eventType,
        description = description,
        userId = user.id.value,
        createdAt = createdAt.toKotlinLocalDateTime()
    )
}
