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
        if (user.name== null) user.rename(googleUserInfo.name)
        if (user.googleId == null) user.changeGoogleId(googleUserInfo.sub)
        if (user.pictureUrl == null) user.changePictureUrl(googleUserInfo.picture)
        user.updatedAt = LocalDateTime.now().toKotlinx()
        userRepo.update(user)
        return user
    }
}