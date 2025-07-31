package dev.byjtech.erp.document_management.infrastructure.exposed.entities

import dev.byjtech.erp.document_management.domain.model.UserNotificationSettings
import dev.byjtech.erp.document_management.infrastructure.exposed.tables.UserNotificationSettingsTable
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class UserNotificationSettingsEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<UserNotificationSettingsEntity>(UserNotificationSettingsTable)

    var userId by UserNotificationSettingsTable.userId
    var companyId by UserNotificationSettingsTable.companyId
    var daysBeforeExpiration by UserNotificationSettingsTable.daysBeforeExpiration
    var emailEnabled by UserNotificationSettingsTable.emailEnabled
    var systemEnabled by UserNotificationSettingsTable.systemEnabled
    var createdAt by UserNotificationSettingsTable.createdAt
    var updatedAt by UserNotificationSettingsTable.updatedAt

    fun toDomain() = UserNotificationSettings(
        id = id.value,
        userId = userId,
        companyId = companyId,
        daysBeforeExpiration = daysBeforeExpiration,
        emailEnabled = emailEnabled,
        systemEnabled = systemEnabled,
        createdAt = createdAt.toKotlinLocalDateTime(),
        updatedAt = updatedAt.toKotlinLocalDateTime()
    )
}
