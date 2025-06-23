package dev.byjtech.erp.core.domain.repository

import dev.byjtech.erp.core.domain.model.Company
import java.util.UUID

interface CompanyRepository {
    fun save(company: Company): Company
    fun findById(id: UUID): Company?
    fun findAll(): List<Company>
    fun delete(id: UUID)
    fun existWithRut(rut: String): Boolean
}