package dev.byjtech.erp.core.domain.repository

import dev.byjtech.erp.core.domain.model.Company

interface CompanyRepository {
    fun save(company: Company): Company
    fun findById(id: Int): Company?
    fun findAll(): List<Company>
    fun delete(id: Int)
}