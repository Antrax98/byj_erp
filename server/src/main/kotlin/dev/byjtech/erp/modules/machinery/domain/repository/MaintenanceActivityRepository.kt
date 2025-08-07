package dev.byjtech.erp.modules.machinery.domain.repository

import dev.byjtech.erp.modules.machinery.domain.model.MaintenanceActivity
import java.util.UUID

interface MaintenanceActivityRepository {
    fun save(activity: MaintenanceActivity): MaintenanceActivity
    fun findById(id: Int): MaintenanceActivity?
    fun findByScheduleId(scheduleId: UUID): List<MaintenanceActivity>
    fun findActiveByScheduleId(scheduleId: UUID): List<MaintenanceActivity>
    fun findAll(): List<MaintenanceActivity>
    fun update(activity: MaintenanceActivity): MaintenanceActivity
    fun delete(id: Int)
    fun findByName(name: String): List<MaintenanceActivity>
}
