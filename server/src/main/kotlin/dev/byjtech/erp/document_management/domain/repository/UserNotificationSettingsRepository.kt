package dev.byjtech.erp.document_management.domain.repository

import dev.byjtech.erp.document_management.domain.model.UserNotificationSettings
import java.util.UUID

interface UserNotificationSettingsRepository {
    fun findByUserId(userId: UUID): UserNotificationSettings?
    fun findByCompanyId(companyId: UUID): List<UserNotificationSettings>
    fun save(settings: UserNotificationSettings): UserNotificationSettings
    fun update(settings: UserNotificationSettings): UserNotificationSettings
    fun createDefaultSettings(userId: UUID, companyId: UUID): UserNotificationSettings
}
