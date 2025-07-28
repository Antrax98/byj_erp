package dev.byjtech.erp.document_management.infrastructure.exposed.tables

import dev.byjtech.erp.document_management.infrastructure.exposed.columns.DocumentStatusColumnType
import dev.byjtech.erp.document_management.infrastructure.exposed.columns.DocumentTypeColumnType
import dev.byjtech.erp.core.infrastructure.exposed.tables.*
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import dev.byjtech.erp.modules.document_management.domain.model.DocumentStatus
import dev.byjtech.erp.modules.document_management.domain.model.DocumentType

object DocumentsTable : UUIDTable("document") {
    val type = registerColumn<DocumentType>("type", DocumentTypeColumnType()) // tipo de documento usando enum
    val documentNumber = varchar("document_number", 255) // numero del documento
    val companyId = uuid("company_id") // empresa asociada (sin restricción FK - referencia a companies en otra DB)
    val issueDate = date("issue_date") // fecha de emision
    val dueDate = date("due_date").nullable() // fecha de vencimiento
    val status = registerColumn<DocumentStatus>("status", DocumentStatusColumnType())// estado del documento
    val currency = text("currency") // moneda usada
    val netAmount = decimal("net_amount", 20, 2) // valor sin impuestos el 20 son los digitos en total y 2 son los decimales
    val taxAmount = decimal("tax_amount", 20, 2) // valor del impuesto
    val totalAmount = decimal("total_amount", 20, 2) // neto + impuesto
    val fileUrl = text("file_url") // ruta al archivo
    val createdBy = uuid("created_by") // usuario que lo creó (sin restricción FK - referencia a users en otra DB)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val active = bool("active").default(true)

    init {
        index(true , type, documentNumber)
    }
}