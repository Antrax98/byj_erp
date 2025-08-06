package dev.byjtech.erp.document_management.infrastructure.exposed.tables

import dev.byjtech.erp.core.infrastructure.exposed.tables.UsersTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import dev.byjtech.erp.document_management.domain.model.DocumentEventType
import dev.byjtech.erp.document_management.infrastructure.exposed.columns.DocumentEventTypeColumnType

object DocumentAuditLogTable : UUIDTable("document_audit_log") {
    val documentId = reference("document_id", DocumentsTable) // ID del documento afectado
    val eventType = registerColumn<DocumentEventType>("event_type", DocumentEventTypeColumnType()) // puede ser desactivado, activado o anulada
    val description = text("description") // descripcion detallada del evento
    val userId = uuid("user_id") // usuario que hizo la edicion
    val createdAt = datetime("created_at")
}
