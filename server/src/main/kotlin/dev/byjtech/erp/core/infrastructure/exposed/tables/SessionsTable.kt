package dev.byjtech.erp.core.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object SessionsTable : IntIdTable("sessions") {
    val userId = reference("user_id", UsersTable)
    //val sessionId = uuid("session_id").uniqueIndex()
    val isValid = bool("is_valid").default(true)
    val accessToken = text("access_token")
    val refreshToken = text("refresh_token")
    val deviceId = varchar("device_id", 255) // Identificador único del dispositivo
    val platform = text("platform") //"web", "android", "ios", "desktop" // inecesario?
    val userAgent = text("user_agent").nullable()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val expiresAt = datetime("expires_at")

    init {
        //evita que haya mas de una session por dispositivo
        index(isUnique = true,
            userId,
            deviceId
        )
    }
}