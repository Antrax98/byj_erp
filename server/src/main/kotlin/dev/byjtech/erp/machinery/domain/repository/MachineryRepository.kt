package dev.byjtech.erp.machinery.domain.repository

import dev.byjtech.erp.machinery.domain.model.Machinery
import java.util.UUID

interface MachineryRepository {
    fun save(machinery: Machinery): Machinery
    fun findById(id: UUID): Machinery?
    fun findByCode(code: String): Machinery?
    fun findByCompanyId(companyId: UUID): List<Machinery>
    fun findAll(): List<Machinery>
    fun findAllActive(): List<Machinery>
    fun findAllInactive(): List<Machinery>
    fun findInactiveByCompanyId(companyId: UUID): List<Machinery>
    fun update(machinery: Machinery): Machinery
    fun delete(id: UUID)
    fun findByStatus(status: String): List<Machinery>
    fun findActiveByCompanyId(companyId: UUID): List<Machinery>
    fun existsWithCode(code: String): Boolean
    fun findByBrandAndModel(brand: String, model: String): List<Machinery>
    fun findBySerialNumber(serialNumber: String): Machinery?
    fun findByLicensePlate(licensePlate: String): Machinery?
}
