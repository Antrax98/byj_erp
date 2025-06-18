package dev.byjtech.erp.core.domain.model

import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class User (
    val id: UUID,
    val name: String?,
    val email: String,
    val googleId: String?,
    val pictureUrl: String?,
    val isActive: Boolean,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?,
    val companyId: UUID?,
    val roles: Set<Role>?,
    val specialPermissions: Set<Permission>?
) {
    fun rename(newName: String) = copy(name = newName)
    fun changeEmail(newEmail: String) = copy(email = newEmail)
    fun changeGoogleId(newGoogleId: String) = copy(googleId = newGoogleId)
    fun changePictureUrl(newPictureUrl: String?) = copy(pictureUrl = newPictureUrl)
    fun changeActive(newIsActive: Boolean) = copy(isActive = newIsActive)
}
