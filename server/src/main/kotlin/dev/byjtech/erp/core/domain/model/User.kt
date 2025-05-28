package dev.byjtech.erp.core.domain.model

import kotlinx.datetime.LocalDateTime

data class User (
    val id: Int,
    val name: String?,
    val email: String,
    val googleId: String?,
    val pictureUrl: String?,
    val isActive: Boolean,
    var createdAt: LocalDateTime?,
    var updatedAt: LocalDateTime?,
    val companyId: Int?,
    val roles: Set<Role>?
) {
    fun rename(newName: String) = copy(name = newName)
    fun changeEmail(newEmail: String) = copy(email = newEmail)
    fun changeGoogleId(newGoogleId: String) = copy(googleId = newGoogleId)
    fun changePictureUrl(newPictureUrl: String?) = copy(pictureUrl = newPictureUrl)
    fun changeActive(newIsActive: Boolean) = copy(isActive = newIsActive)
}
