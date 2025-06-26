package dev.byjtech.erp.modules.document_management.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import dev.byjtech.erp.core.infrastructure.exposed.tables.UsersTable
import dev.byjtech.erp.modules.document_management.infrastructure.exposed.tables.DocumentsTable

object CreditNotesTable : IntIdTable("credit_notes") {
    //val invoiceId = reference("invoice_id", DocumentsTable) // ID de la factura que se va a anulada
    //val creditDocumentId = reference("credit_document_id", DocumentsTable) // ID del documento de tipo nota de credito que anula la factura 
    //esos dos de arriba se referencian al mismo documento pero es porque en la tabla de documentos se almacenan todos los tipos de documentos
    //val userId = reference("user_id", UsersTable) // usuario que registró la nota de credito
    val createdAt = datetime("created_at")

    init {
        index(true,) // invoiceId, creditDocumentId
    }
}

//esta tabla es para el futuro