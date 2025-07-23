package dev.byjtech.erp.core.application.service

import dev.byjtech.erp.config.UserInfo //TODO: intyentar moverlo a un ¿modelDTO?
import dev.byjtech.erp.core.domain.model.User
import dev.byjtech.erp.core.domain.repository.UserRepository
import dev.byjtech.erp.utils.datetime.toKotlinx
import java.time.LocalDateTime

class UserService(
    private val userRepo: UserRepository,
) {
    fun updateUserFromGoogleInfo(user: User, googleUserInfo: UserInfo): User {
        var updatedUser = user

        if (updatedUser.name == null) updatedUser = updatedUser.rename(googleUserInfo.name)
        if (updatedUser.pictureUrl == null) updatedUser = updatedUser.changePictureUrl(googleUserInfo.picture)

        updatedUser = updatedUser.copy(updatedAt = LocalDateTime.now().toKotlinx())

        userRepo.update(updatedUser)
        return updatedUser
    }

}