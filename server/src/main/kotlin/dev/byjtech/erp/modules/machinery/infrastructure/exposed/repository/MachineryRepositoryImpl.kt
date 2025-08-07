package dev.byjtech.erp.modules.machinery.infrastructure.exposed.repository

import dev.byjtech.erp.modules.machinery.domain.model.Machinery
import dev.byjtech.erp.modules.machinery.domain.repository.MachineryRepository
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities.MachineryEntity
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.extensions.fromModel
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.extensions.toModel
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MachineriesTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.time.LocalDateTime
import java.util.UUID

class MachineryRepositoryImpl(private val db: Database) : MachineryRepository {
    
    override fun save(machinery: Machinery): Machinery {
        return transaction(db) {
            val entity = MachineryEntity.new {
                code = machinery.code
                name = machinery.name
                description = machinery.description
                brand = machinery.brand
                model = machinery.model
                year = machinery.year
                serialNumber = machinery.serialNumber
                licensePlate = machinery.licensePlate
                status = machinery.status
                location = machinery.location
                companyId = machinery.companyId
                createdAt = LocalDateTime.now()
                updatedAt = LocalDateTime.now()
                isActive = machinery.isActive
            }
            entity.toModel()
        }
    }

    override fun findById(id: UUID): Machinery? {
        return transaction(db) {
            MachineryEntity.findById(id)?.toModel()
        }
    }

    override fun findByCode(code: String): Machinery? {
        return transaction(db) {
            MachineryEntity.find { MachineriesTable.code eq code }.firstOrNull()?.toModel()
        }
    }

    override fun findByCompanyId(companyId: UUID): List<Machinery> {
        return transaction(db) {
            MachineryEntity.find { MachineriesTable.companyId eq companyId }.map { it.toModel() }
        }
    }

    override fun findAll(): List<Machinery> {
        return transaction(db) {
            MachineryEntity.all().map { it.toModel() }
        }
    }

    override fun findAllActive(): List<Machinery> {
        return transaction(db) {
            MachineryEntity.find { MachineriesTable.isActive eq true }.map { it.toModel() }
        }
    }

    override fun findAllInactive(): List<Machinery> {
        return transaction(db) {
            MachineryEntity.find { MachineriesTable.isActive eq false }.map { it.toModel() }
        }
    }

    override fun findInactiveByCompanyId(companyId: UUID): List<Machinery> {
        return transaction(db) {
            MachineryEntity.find { 
                (MachineriesTable.companyId eq companyId) and (MachineriesTable.isActive eq false) 
            }.map { it.toModel() }
        }
    }

    override fun update(machinery: Machinery): Machinery {
        return transaction(db) {
            val entity = MachineryEntity.findById(machinery.id)
                ?: throw IllegalArgumentException("Machinery with id ${machinery.id} not found")
            entity.fromModel(machinery)
            entity.updatedAt = LocalDateTime.now()
            entity.toModel()
        }
    }

    override fun delete(id: UUID) {
        transaction(db) {
            MachineryEntity.findById(id)?.delete()
        }
    }

    override fun findByStatus(status: String): List<Machinery> {
        return transaction(db) {
            MachineryEntity.find { MachineriesTable.status eq status }.map { it.toModel() }
        }
    }

    override fun findActiveByCompanyId(companyId: UUID): List<Machinery> {
        return transaction(db) {
            MachineryEntity.find { 
                (MachineriesTable.companyId eq companyId) and (MachineriesTable.isActive eq true) 
            }.map { it.toModel() }
        }
    }

    override fun existsWithCode(code: String): Boolean {
        return transaction(db) {
            !MachineryEntity.find { MachineriesTable.code eq code }.empty()
        }
    }

    override fun findByBrandAndModel(brand: String, model: String): List<Machinery> {
        return transaction(db) {
            MachineryEntity.find { 
                (MachineriesTable.brand eq brand) and (MachineriesTable.model eq model) 
            }.map { it.toModel() }
        }
    }

    override fun findBySerialNumber(serialNumber: String): Machinery? {
        return transaction(db) {
            MachineryEntity.find { MachineriesTable.serialNumber eq serialNumber }.firstOrNull()?.toModel()
        }
    }

    override fun findByLicensePlate(licensePlate: String): Machinery? {
        return transaction(db) {
            MachineryEntity.find { MachineriesTable.licensePlate eq licensePlate }.firstOrNull()?.toModel()
        }
    }
}
