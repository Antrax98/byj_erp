package dev.byjtech.erp.core.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

data class User (
    val id: UUID = UUID.randomUUID(),
    val name: String? = null,
    val email: String,
    val googleId: String? = null,
    val pictureUrl: String? = null,
    val isActive: Boolean = true,
    var createdAt: LocalDateTime? = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    var updatedAt: LocalDateTime? = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val companyId: UUID? = null,
    val roles: Set<Role>? = null,
    val specialPermissions: Set<Permission>? = null
) {
    fun rename(newName: String) = copy(name = newName)
    fun changeEmail(newEmail: String) = copy(email = newEmail)
    fun changeGoogleId(newGoogleId: String) = copy(googleId = newGoogleId)
    fun changePictureUrl(newPictureUrl: String?) = copy(pictureUrl = newPictureUrl)
    fun changeActive(newIsActive: Boolean) = copy(isActive = newIsActive)
}
