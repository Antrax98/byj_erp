package dev.byjtech.erp.core.database.companyModule

import dev.byjtech.erp.core.dto.companyModule.CompanyModuleDTO

fun dev.byjtech.erp.core.database.companyModule.CompanyModuleEntity.toDTO(): dev.byjtech.erp.core.dto.companyModule.CompanyModuleDTO {
    return dev.byjtech.erp.core.dto.companyModule.CompanyModuleDTO(
        id = this.id.value,
        companyId = this.company.id.value,
        moduleId = this.module.id.value,
        isActive = this.isActive,
        isAccessible = this.isAccessible
    )
}