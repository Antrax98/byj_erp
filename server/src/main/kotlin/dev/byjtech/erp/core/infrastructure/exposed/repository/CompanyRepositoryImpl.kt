package dev.byjtech.erp.core.infrastructure.exposed.repository

import dev.byjtech.erp.core.domain.model.Company
import dev.byjtech.erp.core.domain.repository.CompanyRepository
import dev.byjtech.erp.core.infrastructure.exposed.entities.CompanyEntity
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toEntity
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toModel
import org.jetbrains.exposed.sql.transactions.transaction

import org.koin.core.Koin.*

class CompanyRepositoryImpl : CompanyRepository {
    override fun save(company: Company): Company = transaction {
        company.toEntity()
        company
    }

    override fun findById(id: Int): Company? = transaction {
        val auxCompany: CompanyEntity? = CompanyEntity.findById(id)
        auxCompany?.toModel() ?: return@transaction null
    }

    override fun findAll(): List<Company> = transaction {
        CompanyEntity.all().map { it.toModel() }
    }

    override fun delete(id: Int) {
        transaction {
            CompanyEntity.findById(id)?.delete()
        }
    }
}