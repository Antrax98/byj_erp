package dev.byjtech.erp.document_management.infrastructure.exposed.columns
import dev.byjtech.erp.document_management.domain.model.DocumentEventType
import org.jetbrains.exposed.sql.ColumnType

class DocumentEventTypeColumnType : ColumnType<DocumentEventType>() {
    override fun sqlType(): String = "VARCHAR(20)"

    override fun valueFromDB(value: Any): DocumentEventType = when (value) {
        is DocumentEventType -> value
        is String -> DocumentEventType.valueOf(value)
        else -> error("Unexpected value for DocumentEventType: $value")
    }

    override fun notNullValueToDB(value: DocumentEventType): Any =
        value.name
}
