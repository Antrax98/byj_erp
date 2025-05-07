package dev.byjtech.erp.core.database.users

import dev.byjtech.erp.core.dto.user.UserDTO
import dev.byjtech.erp.utils.datetime.toKotlinx

fun UserEntity.toDTO(): UserDTO {
    return UserDTO(
        id = this.id.value,
        name = this.name,
        email = this.email,
        pictureUrl = this.pictureUrl,
        lastLoginAt = this.lastLoginAt?.toKotlinx(),
        isActive = this.isActive,
        googleId = this.googleId,
        createdAt = this.createdAt?.toKotlinx(),
        createdBy = this.createdBy?.id?.value,
        updatedAt = this.updatedAt?.toKotlinx(),
        updatedBy = this.updatedBy?.id?.value,
        companyId = this.company?.id?.value
    )
}