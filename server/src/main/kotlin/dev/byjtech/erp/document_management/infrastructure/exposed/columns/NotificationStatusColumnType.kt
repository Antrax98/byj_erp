package dev.byjtech.erp.document_management.infrastructure.exposed.columns

import dev.byjtech.erp.document_management.domain.model.NotificationStatus
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.vendors.currentDialect

class NotificationStatusColumnType : ColumnType<NotificationStatus>() {
    override fun sqlType(): String = "VARCHAR(50)"
    override fun valueFromDB(value: Any): NotificationStatus = NotificationStatus.valueOf(value.toString())
    override fun valueToDB(value: NotificationStatus?): String? = value?.name
}
