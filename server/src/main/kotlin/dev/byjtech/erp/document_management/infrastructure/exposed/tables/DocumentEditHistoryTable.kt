package dev.byjtech.erp.modules.document_management.infrastructure.exposed.tables

import dev.byjtech.erp.core.infrastructure.exposed.tables.UsersTable
import org.jetbrains.exposed.dao.id.IntIdTable  //CAMBIARRR AL HACER FETCH
import org.jetbrains.exposed.sql.javatime.datetime

object DocumentEditHistoryTable : IntIdTable("document_edit_history") {
    //val documentId = reference("document_id", DocumentsTable) // ID del documento editado
    val fieldName = text("field_name") // nombre del campo que fue modificado
    val oldValue = text("old_value").nullable() // valor anterior del campo puede ser nulo
    val newValue = text("new_value").nullable() // nuevo valor del campo puede ser nulo
    //val userId = reference("user_id", UsersTable) // usuario que hizo la edicion
    val createdAt = datetime("created_at")
    }
