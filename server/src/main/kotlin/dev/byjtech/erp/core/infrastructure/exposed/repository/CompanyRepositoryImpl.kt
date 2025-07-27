package dev.byjtech.erp.core.infrastructure.exposed.repository

import dev.byjtech.erp.core.domain.model.Company
import dev.byjtech.erp.core.domain.repository.CompanyRepository
import dev.byjtech.erp.core.infrastructure.exposed.entities.CompanyEntity
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toEntity
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toModel
import dev.byjtech.erp.core.infrastructure.exposed.tables.CompaniesTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.transaction

import org.koin.core.Koin.*
import java.util.UUID

class CompanyRepositoryImpl(private val db: Database) : CompanyRepository {
    override fun save(company: Company): Company = transaction(db) {
        val newCompany = company.toEntity()
        newCompany.toModel()
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

    override fun existWithRut(rut: String): Boolean = transaction(db) {
        CompanyEntity.find { CompaniesTable.rut eq rut }.firstOrNull() != null
    }

    override fun updateName(id: UUID, newName: String) {
        transaction(db) {
            CompanyEntity.findById(id)?.name = newName
        }

    }

    override fun updateContactEmail(id: UUID, newEmail: String) {
        transaction(db) {
            CompanyEntity.findById(id)?.contactEmail = newEmail
        }
    }
}