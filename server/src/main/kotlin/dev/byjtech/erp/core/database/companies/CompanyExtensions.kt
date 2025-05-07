package dev.byjtech.erp.core.database.companies

import dev.byjtech.erp.core.dto.company.CompanyDTO
import dev.byjtech.erp.utils.datetime.toKotlinx

fun dev.byjtech.erp.core.database.companies.CompanyEntity.toDTO(): dev.byjtech.erp.core.dto.company.CompanyDTO {
    return dev.byjtech.erp.core.dto.company.CompanyDTO(
        id = this.id.value,
        name = this.name,
        contactEmail = this.contactEmail,
        createdAt = this.createdAt.toKotlinx(),
        updatedAt = this.updatedAt.toKotlinx()
    )
}