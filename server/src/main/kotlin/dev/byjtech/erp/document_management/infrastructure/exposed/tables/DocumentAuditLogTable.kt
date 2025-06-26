package dev.byjtech.erp.modules.document_management.infrastructure.exposed.tables

import dev.byjtech.erp.core.infrastructure.exposed.tables.UsersTable
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import dev.byjtech.erp.modules.document_management.domain.model.DocumentEventType

object DocumentAuditLogTable : IntIdTable("document_audit_log") {
    //val documentId = reference("document_id", DocumentsTable) // ID del documento afectado
    val eventType = text("event_type") // puede ser desactivado, activado o anulada
    val description = text("description") // descripcion detallada del evento
    //val userId = reference("user_id", UsersTable) // usuario que hizo la edicion
    val createdAt = datetime("created_at")
}
