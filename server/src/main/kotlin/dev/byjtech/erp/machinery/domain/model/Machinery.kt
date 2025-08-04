package dev.byjtech.erp.machinery.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

data class Machinery(
    val id: UUID = UUID.randomUUID(),
    val code: String,
    val name: String,
    val description: String? = null,
    val brand: String,
    val model: String,
    val year: Int? = null,
    val serialNumber: String? = null,
    val licensePlate: String? = null,
    val status: String, // ACTIVE, INACTIVE, MAINTENANCE, etc.
    val location: String? = null,
    val companyId: UUID,
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val updatedAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val isActive: Boolean = true
) {
    fun updateName(newName: String) = copy(name = newName)
    fun updateDescription(newDescription: String?) = copy(description = newDescription)
    fun updateStatus(newStatus: String) = copy(status = newStatus)
    fun updateLocation(newLocation: String?) = copy(location = newLocation)
    fun activate() = copy(isActive = true)
    fun deactivate() = copy(isActive = false)
    fun updateSerialNumber(newSerialNumber: String?) = copy(serialNumber = newSerialNumber)
    fun updateLicensePlate(newLicensePlate: String?) = copy(licensePlate = newLicensePlate)
}
