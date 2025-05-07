package dev.byjtech.erp.core.database.companyModule

import dev.byjtech.erp.core.database.companies.Companies
import dev.byjtech.erp.core.database.modules.Modules
import org.jetbrains.exposed.dao.id.IntIdTable

object CompanyModule : IntIdTable("company_module") {
    val companyId = reference("company_id", Companies)
    val moduleId = reference("module_id", Modules)
    val isActive = bool("is_active").default(true)
    val isAccessible = bool("is_accessible").default(true)

    init {
        // Esto se asegurara de que no haya una combinacion repetida de compañia y modulo
        // ya que una compañia solo deveria contratar una vez el mismo modulo
        index(true,
            companyId,
            moduleId
        )

        //foreignKey("fk_company", companyId, Companies.id)
        //foreignKey("fk_module", moduleId, reference = Modules)
    }
}