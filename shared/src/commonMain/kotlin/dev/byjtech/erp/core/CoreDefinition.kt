package dev.byjtech.erp.core

import dev.byjtech.erp.common.*

object CoreDefinition : ModuleDefinition {
    override val name = "core"
    override val displayName = "Core"
    override val description = "Core del sistema"
    override val developerOnly = false

    object Users : CategoryBase("users", "User management", CoreDefinition) {
        val Create = permission("create", "Create a new user")
        val View   = permission("view", "View a user")
        val Update = permission("update", "Update a user")
        val Delete = permission("delete", "Delete a user")
    }

    object Roles : CategoryBase("roles", "Role and permission management", CoreDefinition) {
        val Create = permission("create", "Create a new role")
        val View   = permission("view", "View a role")
        val Update = permission("update", "Update a role")
        val Delete = permission("delete", "Delete a role")
        val Assign = permission("assign", "Assign a role or permissions to a user")
        val Unassign = permission("unassign", "Unassign a role or permissions from a user")
    }

    object Companies : CategoryBase("companies", "Company management", CoreDefinition) {
        val Create = permission("create", "Create a new company")
        val View   = permission("view", "View a company")
        val Update = permission("update", "Update a company")
        val Delete = permission("delete", "Delete a company")

    }

    object Subscriptions : CategoryBase("subscriptions", "Subscription management", CoreDefinition) {
        val Create = permission("create", "Create a new subscription")
        val View   = permission("view", "View a subscription")
        val Update = permission("update", "Update a subscription")
        val Delete = permission("delete", "Delete a subscription")
    }

    object Admin : CategoryBase("admin", "Admin permissions", CoreDefinition) {
        val All = permission("all", "Does everything company level")
    }

    override val categories = setOf(Users, Roles, Companies, Subscriptions, Admin)
}