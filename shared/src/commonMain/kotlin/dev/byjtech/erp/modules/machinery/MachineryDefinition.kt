package dev.byjtech.erp.modules.machinery

import dev.byjtech.erp.common.CategoryBase
import dev.byjtech.erp.common.ModuleDefinition

object MachineryDefinition : ModuleDefinition {
    override val name = "machinery"
    override val displayName = "Machinery"
    override val description = "Machinery and vehicle management system"
    override val developerOnly = false

    object Machinery : CategoryBase("machinery", "Machinery management", MachineryDefinition) {
        val Create = permission("create", "Create a new machinery")
        val View = permission("view", "View machinery details")
        val Update = permission("update", "Update machinery information")
        val Delete = permission("delete", "Delete or deactivate machinery")
        val History = permission("history", "View machinery change history")
    }

    object Documents : CategoryBase("documents", "Document management", MachineryDefinition) {
        val Upload = permission("upload", "Upload documents for machinery")
        val View = permission("view", "View machinery documents")
        val Update = permission("update", "Update document information")
        val Delete = permission("delete", "Delete documents")
    }

    object Maintenance : CategoryBase("maintenance", "Maintenance management", MachineryDefinition) {
        val CreateSchedule = permission("create_schedule", "Create maintenance schedules")
        val ViewSchedule = permission("view_schedule", "View maintenance schedules")
        val UpdateSchedule = permission("update_schedule", "Update maintenance schedules")
        val DeleteSchedule = permission("delete_schedule", "Delete maintenance schedules")
        val CreateOrder = permission("create_order", "Create work orders")
        val ViewOrder = permission("view_order", "View work orders")
        val UpdateOrder = permission("update_order", "Update work orders")
        val DeleteOrder = permission("delete_order", "Delete work orders")
    }

    override val categories = setOf(Machinery, Documents, Maintenance)
}
