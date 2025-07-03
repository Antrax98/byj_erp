package dev.byjtech.erp.modules.document_management.infrastructure.exposed.columns

import dev.byjtech.erp.modules.document_management.domain.model.DocumentStatus
import org.jetbrains.exposed.sql.ColumnType

class DocumentStatusColumnType : ColumnType() {
    override fun sqlType(): String = "VARCHAR(20)"

    override fun valueFromDB(value: Any): DocumentStatus = when (value) {
        is DocumentStatus -> value
        is String -> DocumentStatus.valueOf(value)
        else -> error("Unexpected value for DocumentStatus: $value")
    }

    override fun notNullValueToDB(value: Any): String =
        (value as DocumentStatus).name
}
