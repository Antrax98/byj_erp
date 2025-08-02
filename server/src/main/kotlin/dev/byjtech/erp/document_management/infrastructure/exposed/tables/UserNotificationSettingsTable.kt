package dev.byjtech.erp.document_management.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object UserNotificationSettingsTable : UUIDTable("user_notification_settings") {
    val userId = uuid("user_id") // referencia a users en core DB
    val companyId = uuid("company_id") // referencia a companies en core DB
    val daysBeforeExpiration = integer("days_before_expiration").default(7)
    val emailEnabled = bool("email_enabled").default(true)
    val systemEnabled = bool("system_enabled").default(true)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")

    init {
        uniqueIndex(userId, companyId)
    }
}
