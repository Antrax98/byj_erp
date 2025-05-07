package dev.byjtech.erp.modules.announcements.v1

import dev.byjtech.erp.core.permissions.ModulePermission

object AnnouncementsV1Permissions {
    const val name = "announcements"
    const val version = "v1"

    object Misc {
        const val NAME = "misc"
        const val DESCRIPTION = "Manage announcements"
        val Create = ModulePermission("create", "Create", name, version, NAME)
        val Read = ModulePermission("read", "Read", name, version, NAME)
        val Edit = ModulePermission("edit", "Edit", name, version, NAME)
        val Delete = ModulePermission("delete", "Delete", name, version, NAME)
        val Pin = ModulePermission("pin", "Pin", name, version, NAME)

        val all = listOf(Create, Read, Edit, Delete, Pin)
    }

}