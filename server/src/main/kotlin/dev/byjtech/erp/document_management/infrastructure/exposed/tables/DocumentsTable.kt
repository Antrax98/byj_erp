package dev.byjtech.erp.modules.document_management.infrastructure.exposed.tables

import dev.byjtech.erp.modules.document_management.infrastructure.exposed.columns.DocumentStatusColumnType
import dev.byjtech.erp.core.infrastructure.exposed.tables.*
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.date
import org.jetbrains.exposed.sql.javatime.datetime
import dev.byjtech.erp.modules.document_management.domain.model.DocumentStatus
//ver user otra base de datos (colocar id)
object DocumentsTable : IntIdTable("documents") {
    val documentType = text("document_type") // tipo de documento
    val documentNumber = text("document_number") // numero del documento
    //val companyId = reference("company_id", CompaniesTable) // empresa asociada
    //val categoryId = reference("category_id", CategoriesTable).nullable() // categoria opcional
    val issueDate = date("issue_date") // fecha de emision
    val dueDate = date("due_date").nullable() // fecha de vencimiento
    val status = registerColumn<DocumentStatus>("status", DocumentStatusColumnType())// estado del documento
    val currency = text("currency") // moneda usada
    val netAmount = decimal("net_amount", 20, 2) // valor sin impuestos el 20 son los digitos en total y 2 son los decimales
    val taxAmount = decimal("tax_amount", 20, 2) // valor del impuesto
    val totalAmount = decimal("total_amount", 20, 2) // neto + impuesto
    val fileUrl = text("file_url") // ruta al archivo
    val createdBy = reference("created_by", UsersTable) // usuario que lo creó
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val active = bool("active").default(true)

    init {
        index(true , documentType, documentNumber)
    }
}