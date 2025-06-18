package dev.byjtech.erp.core.infrastructure.exposed.repository

import dev.byjtech.erp.core.domain.model.Company
import dev.byjtech.erp.core.domain.repository.CompanyRepository
import dev.byjtech.erp.core.infrastructure.exposed.entities.CompanyEntity
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toEntity
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toModel
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

import org.koin.core.Koin.*
import java.util.UUID

class CompanyRepositoryImpl(private val db: Database) : CompanyRepository {
    override fun save(company: Company): Company = transaction(db) {
        company.toEntity()
        company
    }

    override fun findById(id: UUID): Company? = transaction(db) {
        val auxCompany: CompanyEntity? = CompanyEntity.findById(id)
        auxCompany?.toModel() ?: return@transaction null
    }

    override fun findAll(): List<Company> = transaction(db) {
        CompanyEntity.all().map { it.toModel() }
    }

    override fun delete(id: UUID) {
        transaction(db) {
            CompanyEntity.findById(id)?.delete()
        }
    }
}