package dev.byjtech.erp.modules.machinery.domain.repository

import dev.byjtech.erp.modules.machinery.domain.model.MaintenanceSchedule
import kotlinx.datetime.LocalDateTime
import java.util.UUID

interface MaintenanceScheduleRepository {
    fun save(schedule: MaintenanceSchedule): MaintenanceSchedule
    fun findById(id: UUID): MaintenanceSchedule?
    fun findByMachineryId(machineryId: UUID): List<MaintenanceSchedule>
    fun findActiveByMachineryId(machineryId: UUID): List<MaintenanceSchedule>
    fun findAll(): List<MaintenanceSchedule>
    fun update(schedule: MaintenanceSchedule): MaintenanceSchedule
    fun delete(id: UUID)
    fun findByCreatedBy(createdBy: UUID): List<MaintenanceSchedule>
    fun findDueSchedules(currentDate: LocalDateTime): List<MaintenanceSchedule>
    fun findOverdueSchedules(currentDate: LocalDateTime): List<MaintenanceSchedule>
    fun findByIntervalType(intervalType: String): List<MaintenanceSchedule>
}
