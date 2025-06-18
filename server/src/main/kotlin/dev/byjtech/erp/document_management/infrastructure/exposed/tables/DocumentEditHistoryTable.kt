package dev.byjtech.erp.modules.document_management.infrastructure.exposed.tables

import dev.byjtech.erp.core.infrastructure.exposed.tables.UsersTable
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object DocumentEditHistoryTable : UUIDTable("document_edit_history") {
    val documentId = reference("document_id", DocumentsTable) // ID del documento editado
    val fieldName = text("field_name") // Nombre del campo que fue modificado
    val oldValue = text("old_value").nullable() // Valor anterior del campo (puede ser nulo)
    val newValue = text("new_value").nullable() // Nuevo valor del campo (puede ser nulo)
    val userId = reference("user_id", UsersTable) // Usuario que realizó la modificación
    val createdAt = datetime("created_at") // Fecha y hora de la modificación
}
