package dev.byjtech.erp.core.infrastructure.exposed.extensions

import dev.byjtech.erp.core.domain.model.Company
import dev.byjtech.erp.core.dto.CompanyDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.CompanyEntity

import dev.byjtech.erp.utils.datetime.toKotlinx
import kotlinx.datetime.toJavaLocalDateTime

fun CompanyEntity.toDTO(): CompanyDTO {
    return CompanyDTO(
        id = this.id.value.toString(),
        name = this.name,
        contactEmail = this.contactEmail,
        createdAt = this.createdAt?.toKotlinx(),
        updatedAt = this.updatedAt?.toKotlinx()
    )
}

fun CompanyEntity.toModel(): Company {
    return Company(
        id = this.id.value,
        name = this.name,
        contactEmail = this.contactEmail,
        createdAt = this.createdAt?.toKotlinx(),
        updatedAt = this.updatedAt?.toKotlinx()
    )
}

//fun Company.toEntity(existingEntity: CompanyEntity? = null): CompanyEntity {
//    val entity = existingEntity ?: CompanyEntity.new()
//    entity.name = this.name
//    entity.contactEmail = this.contactEmail
//    entity.createdAt = this.createdAt.toJavaLocalDateTime()
//    entity.updatedAt = this.updatedAt.toJavaLocalDateTime()
//    return entity
//}

//solo usar en el CompanyRepository
//y dentro de un transaction//mejor no usarla y hacerlo a mano
fun Company.toEntity(existingEntity: CompanyEntity? = null): CompanyEntity {
    return if (existingEntity == null) {
        CompanyEntity.new(null) {
            name = this@toEntity.name
            contactEmail = this@toEntity.contactEmail
            createdAt = this@toEntity.createdAt?.toJavaLocalDateTime()
            updatedAt = this@toEntity.updatedAt?.toJavaLocalDateTime()
        }
    } else {
        existingEntity.name = this.name
        existingEntity.contactEmail = this.contactEmail
        existingEntity.createdAt = this.createdAt?.toJavaLocalDateTime()
        existingEntity.updatedAt = this.updatedAt?.toJavaLocalDateTime()
        existingEntity
    }
}

fun Company.toDTO(): CompanyDTO {
    return CompanyDTO(
        id = this.id.toString(),
        name = this.name,
        contactEmail = this.contactEmail,
        createdAt = this.createdAt,
        updatedAt = this.updatedAt
    )
}