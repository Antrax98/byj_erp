package dev.byjtech.erp.core.infrastructure.exposed.extensions

import dev.byjtech.erp.core.domain.model.User
import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.CompanyEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.UserEntity
import dev.byjtech.erp.utils.datetime.toKotlinx
import kotlinx.datetime.toJavaLocalDateTime

fun UserEntity.toDTO(): UserDTO {
    return UserDTO(
        id = this.id.value,
        name = this.name,
        email = this.email,
        pictureUrl = this.pictureUrl,
        isActive = this.isActive,
        googleId = this.googleId,
        createdAt = this.createdAt?.toKotlinx(),
        updatedAt = this.updatedAt?.toKotlinx(),
        companyId = this.company?.id?.value
    )
}

fun UserEntity.toDomain(): User {
    return User(
        id = this.id.value,
        name = this.name,
        email = this.email,
        googleId = this.googleId,
        pictureUrl = this.pictureUrl,
        isActive = this.isActive,
        createdAt = this.createdAt?.toKotlinx(),
        updatedAt = this.updatedAt?.toKotlinx(),
        companyId = this.company?.id?.value,
        roles = emptySet()
    )
}

fun UserEntity.fromDomain(user: User) {
    this.name = user.name
    this.email = user.email
    this.googleId = user.googleId
    this.pictureUrl = user.pictureUrl
    this.isActive = user.isActive
    this.createdAt = user.createdAt?.toJavaLocalDateTime()
    this.updatedAt = user.updatedAt?.toJavaLocalDateTime()
    this.company = user.companyId?.let { CompanyEntity.findById(it) }
}