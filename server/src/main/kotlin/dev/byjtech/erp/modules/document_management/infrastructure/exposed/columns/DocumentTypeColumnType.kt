package dev.byjtech.erp.document_management.infrastructure.exposed.columns

import dev.byjtech.erp.modules.document_management.domain.model.DocumentType
import org.jetbrains.exposed.sql.ColumnType

class DocumentTypeColumnType : ColumnType<DocumentType>() {
    override fun sqlType(): String = "VARCHAR(255)"

    override fun valueFromDB(value: Any): DocumentType = when (value) {
        is DocumentType -> value
        is String -> DocumentType.valueOf(value)
        else -> error("Unexpected value for DocumentType: $value")
    }

    override fun notNullValueToDB(value: DocumentType): Any =
        value.name
}
