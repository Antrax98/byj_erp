package dev.byjtech.erp.core

import dev.byjtech.erp.core.permissions.ModulePermission


object CoreCorePermissions {
    const val name = "core"
    const val version = "core"

    object Users {
        const val NAME = "users"
        const val DESCRIPTION = "Manage users"
        val Create = ModulePermission("create", "Create a new users",
            name, version, NAME
        )
        val Read   = ModulePermission("view", "View an user",
            name, version, NAME
        )
        val Update = ModulePermission("update", "Update an user",
            name, version, NAME
        )
        val Delete = ModulePermission("delete", "Delete an user",
            name, version, NAME
        )

        val all = listOf(Users.Create,Users.Read,Users.Update,Users.Delete)
    }
    object Admin {
        const val NAME = "admin"
        const val DESCRIPTION = "Manage admin permissions"
        val All = ModulePermission("all", "Does everything company level", name, version, NAME)

        val all = listOf(Admin.All)
    }
}