package dev.byjtech.erp.core.infrastructure.exposed.tables

import dev.byjtech.erp.core.session.AuthProviderType
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime

object AuthenticationsTable: UUIDTable("auth_identities") {
    val userId = reference("user_id", UsersTable)
    val providerType = enumerationByName("type", 50, AuthProviderType::class)
    val providerKey = char("provider_key",255)//ej: googleID o correo para password
    val providerToken = text("provider_token").nullable()//token refresco
    val hashedPassword = text("hashed_password").nullable()
    val salt = text("salt").nullable()
    val createdAt = datetime("created_at").nullable()
    val updatedAt = datetime("updated_at").nullable()

    init {
        uniqueIndex("unique_user_provider", userId, providerType, providerKey)
    }
}

