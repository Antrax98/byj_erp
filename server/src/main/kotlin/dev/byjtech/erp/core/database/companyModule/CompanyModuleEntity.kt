package dev.byjtech.erp.core.database.companyModule

import dev.byjtech.erp.core.database.companies.CompanyEntity
import dev.byjtech.erp.core.database.modules.ModuleEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class CompanyModuleEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<CompanyModuleEntity>(
        CompanyModule
    )

    var company by CompanyEntity referencedOn CompanyModule.companyId
    var module by ModuleEntity referencedOn CompanyModule.moduleId
    var isActive by dev.byjtech.erp.core.database.companyModule.CompanyModule.isActive
    var isAccessible by dev.byjtech.erp.core.database.companyModule.CompanyModule.isAccessible
}