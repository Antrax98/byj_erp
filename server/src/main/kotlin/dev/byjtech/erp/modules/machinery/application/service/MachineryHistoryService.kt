package dev.byjtech.erp.modules.machinery.application.service

import dev.byjtech.erp.modules.machinery.domain.model.Machinery
import dev.byjtech.erp.modules.machinery.domain.model.MachineryHistory
import dev.byjtech.erp.modules.machinery.domain.repository.MachineryHistoryRepository
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

class MachineryHistoryService(
    private val machineryHistoryRepository: MachineryHistoryRepository
) {
    
    fun getHistoryByMachineryId(machineryId: UUID): List<MachineryHistory> {
        return machineryHistoryRepository.findByMachineryId(machineryId)
    }
    
    fun getHistoryByUserId(userId: UUID): List<MachineryHistory> {
        return machineryHistoryRepository.findByUserId(userId)
    }
    
    fun getHistoryByField(field: String): List<MachineryHistory> {
        return machineryHistoryRepository.findByField(field)
    }
    
    fun logMachineryCreation(machinery: Machinery, userId: UUID): MachineryHistory {
        val history = MachineryHistory(
            machineryId = machinery.id,
            userId = userId,
            field = "CREATED",
            oldValue = null,
            newValue = "Machinery created: ${machinery.name}",
            comment = "Initial machinery creation"
        )
        return machineryHistoryRepository.save(history)
    }
    
    fun logMachineryUpdate(
        machineryId: UUID,
        userId: UUID,
        oldMachinery: Machinery,
        newMachinery: Machinery
    ): List<MachineryHistory> {
        val historyEntries = mutableListOf<MachineryHistory>()
        
        // Comparar campos y registrar cambios
        if (oldMachinery.name != newMachinery.name) {
            historyEntries.add(createHistoryEntry(
                machineryId, userId, "name", oldMachinery.name, newMachinery.name
            ))
        }
        
        if (oldMachinery.description != newMachinery.description) {
            historyEntries.add(createHistoryEntry(
                machineryId, userId, "description", oldMachinery.description, newMachinery.description
            ))
        }
        
        if (oldMachinery.brand != newMachinery.brand) {
            historyEntries.add(createHistoryEntry(
                machineryId, userId, "brand", oldMachinery.brand, newMachinery.brand
            ))
        }
        
        if (oldMachinery.model != newMachinery.model) {
            historyEntries.add(createHistoryEntry(
                machineryId, userId, "model", oldMachinery.model, newMachinery.model
            ))
        }
        
        if (oldMachinery.year != newMachinery.year) {
            historyEntries.add(createHistoryEntry(
                machineryId, userId, "year", oldMachinery.year?.toString(), newMachinery.year?.toString()
            ))
        }
        
        if (oldMachinery.serialNumber != newMachinery.serialNumber) {
            historyEntries.add(createHistoryEntry(
                machineryId, userId, "serialNumber", oldMachinery.serialNumber, newMachinery.serialNumber
            ))
        }
        
        if (oldMachinery.licensePlate != newMachinery.licensePlate) {
            historyEntries.add(createHistoryEntry(
                machineryId, userId, "licensePlate", oldMachinery.licensePlate, newMachinery.licensePlate
            ))
        }
        
        if (oldMachinery.status != newMachinery.status) {
            historyEntries.add(createHistoryEntry(
                machineryId, userId, "status", oldMachinery.status, newMachinery.status
            ))
        }
        
        if (oldMachinery.location != newMachinery.location) {
            historyEntries.add(createHistoryEntry(
                machineryId, userId, "location", oldMachinery.location, newMachinery.location
            ))
        }
        
        // Guardar todos los cambios
        return historyEntries.map { machineryHistoryRepository.save(it) }
    }
    
    fun logMachineryDeactivation(machineryId: UUID, userId: UUID): MachineryHistory {
        val history = MachineryHistory(
            machineryId = machineryId,
            userId = userId,
            field = "isActive",
            oldValue = "true",
            newValue = "false",
            comment = "Machinery deactivated"
        )
        return machineryHistoryRepository.save(history)
    }
    
    fun logMachineryActivation(machineryId: UUID, userId: UUID): MachineryHistory {
        val history = MachineryHistory(
            machineryId = machineryId,
            userId = userId,
            field = "isActive",
            oldValue = "false",
            newValue = "true",
            comment = "Machinery activated"
        )
        return machineryHistoryRepository.save(history)
    }
    
    fun logMaintenanceStart(machineryId: UUID, userId: UUID, comment: String? = null): MachineryHistory {
        val history = MachineryHistory(
            machineryId = machineryId,
            userId = userId,
            field = "maintenance",
            oldValue = null,
            newValue = "MAINTENANCE_STARTED",
            comment = comment ?: "Maintenance started"
        )
        return machineryHistoryRepository.save(history)
    }
    
    fun logMaintenanceEnd(machineryId: UUID, userId: UUID, comment: String? = null): MachineryHistory {
        val history = MachineryHistory(
            machineryId = machineryId,
            userId = userId,
            field = "maintenance",
            oldValue = "MAINTENANCE_STARTED",
            newValue = "MAINTENANCE_COMPLETED",
            comment = comment ?: "Maintenance completed"
        )
        return machineryHistoryRepository.save(history)
    }
    
    fun addCustomHistoryEntry(
        machineryId: UUID,
        userId: UUID,
        field: String,
        oldValue: String? = null,
        newValue: String? = null,
        comment: String? = null
    ): MachineryHistory {
        val history = MachineryHistory(
            machineryId = machineryId,
            userId = userId,
            field = field,
            oldValue = oldValue,
            newValue = newValue,
            comment = comment
        )
        return machineryHistoryRepository.save(history)
    }
    
    private fun createHistoryEntry(
        machineryId: UUID,
        userId: UUID,
        field: String,
        oldValue: String?,
        newValue: String?
    ): MachineryHistory {
        return MachineryHistory(
            machineryId = machineryId,
            userId = userId,
            field = field,
            oldValue = oldValue,
            newValue = newValue,
            comment = "Field '$field' changed from '$oldValue' to '$newValue'"
        )
    }
}
