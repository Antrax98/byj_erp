package dev.byjtech.erp.document_management.infrastructure.exposed.columns

import dev.byjtech.erp.document_management.domain.model.NotificationType
import org.jetbrains.exposed.sql.ColumnType
import org.jetbrains.exposed.sql.vendors.currentDialect

class NotificationTypeColumnType : ColumnType<NotificationType>() {
    override fun sqlType(): String = "VARCHAR(50)"
    override fun valueFromDB(value: Any): NotificationType = NotificationType.valueOf(value.toString())
    override fun valueToDB(value: NotificationType?): String? = value?.name
}
