package dev.byjtech.erp.document_management.infrastructure.exposed.repository

import dev.byjtech.erp.document_management.domain.model.UserNotificationSettings
import dev.byjtech.erp.document_management.domain.repository.UserNotificationSettingsRepository
import dev.byjtech.erp.document_management.infrastructure.exposed.entities.UserNotificationSettingsEntity
import dev.byjtech.erp.document_management.infrastructure.exposed.tables.UserNotificationSettingsTable
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class UserNotificationSettingsRepositoryImpl(
    private val database: Database
) : UserNotificationSettingsRepository {

    override fun findByUserId(userId: UUID): UserNotificationSettings? = transaction(database) {
        UserNotificationSettingsEntity.find { UserNotificationSettingsTable.userId eq userId }
            .singleOrNull()?.toDomain()
    }

    override fun findByCompanyId(companyId: UUID): List<UserNotificationSettings> = transaction(database) {
        UserNotificationSettingsEntity.find { UserNotificationSettingsTable.companyId eq companyId }
            .map { it.toDomain() }
    }

    override fun save(settings: UserNotificationSettings): UserNotificationSettings = transaction(database) {
        val entity = UserNotificationSettingsEntity.new {
            userId = settings.userId
            companyId = settings.companyId
            daysBeforeExpiration = settings.daysBeforeExpiration
            emailEnabled = settings.emailEnabled
            systemEnabled = settings.systemEnabled
            createdAt = settings.createdAt.toJavaLocalDateTime()
            updatedAt = settings.updatedAt.toJavaLocalDateTime()
        }
        entity.toDomain()
    }

    override fun update(settings: UserNotificationSettings): UserNotificationSettings = transaction(database) {
        val entity = UserNotificationSettingsEntity[settings.id]
        entity.daysBeforeExpiration = settings.daysBeforeExpiration
        entity.emailEnabled = settings.emailEnabled
        entity.systemEnabled = settings.systemEnabled
        entity.updatedAt = settings.updatedAt.toJavaLocalDateTime()
        entity.toDomain()
    }

    override fun createDefaultSettings(userId: UUID, companyId: UUID): UserNotificationSettings = transaction(database) {
        val currentTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
        
        val settings = UserNotificationSettings(
            id = UUID.randomUUID(),
            userId = userId,
            companyId = companyId,
            daysBeforeExpiration = 7, // Por defecto 7 días
            emailEnabled = true,
            systemEnabled = true,
            createdAt = currentTime,
            updatedAt = currentTime
        )
        
        save(settings)
    }
}
