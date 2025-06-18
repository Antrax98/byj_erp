package dev.byjtech.erp.core.infrastructure.exposed.extensions

import dev.byjtech.erp.core.domain.model.Permission
import dev.byjtech.erp.core.domain.model.User
import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.CompanyEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.RoleEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.UserEntity
import dev.byjtech.erp.utils.datetime.toKotlinx
import kotlinx.datetime.toJavaLocalDateTime

//no usar de ser posible, solo como ejemplo
fun UserEntity.toDTO(): UserDTO {
    return UserDTO(
        id = this.id.value.toString(),
        name = this.name,
        email = this.email,
        pictureUrl = this.pictureUrl,
        isActive = this.isActive,
        googleId = this.googleId,
        createdAt = this.createdAt?.toKotlinx(),
        updatedAt = this.updatedAt?.toKotlinx(),
        companyId = this.company?.id?.value.toString()
    )
}

fun UserEntity.toModel(rolesSet: Set<RoleEntity>? = null, specialSet: Set<Permission>? = null): User {
    val roles = rolesSet?.map { it.toModel() }?.toSet()
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
        roles = roles?:emptySet(),
        specialPermissions = specialSet?:emptySet()
    )
}

fun UserEntity.fromModel(user: User) {
    this.name = user.name
    this.email = user.email
    this.googleId = user.googleId
    this.pictureUrl = user.pictureUrl
    this.isActive = user.isActive
    this.createdAt = user.createdAt?.toJavaLocalDateTime()
    this.updatedAt = user.updatedAt?.toJavaLocalDateTime()
    this.company = user.companyId?.let { CompanyEntity.findById(it) }
}

fun User.toDTO(): UserDTO {
    return UserDTO(
        id = this.id.toString(),
        name = this.name,
        email = this.email,
        pictureUrl = this.pictureUrl,
        isActive = this.isActive,
        googleId = this.googleId,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt,
        companyId = this.companyId.toString()
    )
}

//fun UserDTO.toModel(): User {
//    return User(
//        id = this.id,
//        name = this.name,
//        email = this.email,
//        pictureUrl = this.pictureUrl,
//        isActive = this.isActive,
//        googleId = this.googleId,
//        createdAt = this.createdAt,
//        updatedAt = this.updatedAt,
//        companyId = this.companyId,
//        roles = emptySet(),
//        specialPermissions = emptySet()
//    )
//}