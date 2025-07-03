package dev.byjtech.erp.machinery.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.UUID

object MachineriesTable : UUIDTable("machineries") {
    val code = varchar("code", 50).uniqueIndex()
    val name = varchar("name", 255)
    val description = text("description").nullable()
    val brand = varchar("brand", 100)
    val model = varchar("model", 100)
    val year = integer("year").nullable()
    val serialNumber = varchar("serial_number", 100).nullable()
    val licensePlate = varchar("license_plate", 100).nullable()
    val status = varchar("status", 20) // ACTIVE, INACTIVE, MAINTENANCE, etc.
    val location = varchar("location", 255).nullable()
    val companyId = uuid("company_id") //EN CASO DE, USAR STRING
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
    val isActive = bool("is_active").default(true)
}
