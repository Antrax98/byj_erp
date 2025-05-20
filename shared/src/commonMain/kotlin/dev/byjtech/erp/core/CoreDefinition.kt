package dev.byjtech.erp.core

import dev.byjtech.erp.common.*


//object CorePermissions {
//    const val name = "core"
//    const val version = "core"
//
//    object Users {
//        const val NAME = "users"
//        const val DESCRIPTION = "Manage users"
//        val Create = ModulePermission("create", "Create a new users",
//            name, version, NAME
//        )
//        val Read   = ModulePermission("view", "View an user",
//            name, version, NAME
//        )
//        val Update = ModulePermission("update", "Update an user",
//            name, version, NAME
//        )
//        val Delete = ModulePermission("delete", "Delete an user",
//            name, version, NAME
//        )
//
//        val all = listOf(Users.Create,Users.Read,Users.Update,Users.Delete)
//    }
//    object Admin {
//        const val NAME = "admin"
//        const val DESCRIPTION = "Manage admin permissions"
//        val All = ModulePermission("all", "Does everything company level", name, version, NAME)
//
//        val all = listOf(Admin.All)
//    }
//}

//object Core: Module {
//    override val name = "core"
//    override val displayName = "Core"
//    override val description = "Core del systema"
//    override val developerOnly = false
//
//    object Users: Category {
//        override val name = "users"
//        override val description = "User magnament"
//
//        object Create: Permission {
//            override val action = "create"
//            override val description = "Create a new users"
//            override val key: PermissionKey
//                get() = TODO("Not yet implemented")
//        }
//        object View: Permission {
//            override val action = "view"
//            override val description = "View an user"
//            override val key: PermissionKey
//                get() = TODO("Not yet implemented")
//        }
//        object Update: Permission {
//            override val action = "update"
//            override val description = "Update an user"
//            override val key: PermissionKey
//                get() = TODO("Not yet implemented")
//        }
//        object Delete: Permission {
//            override val action = "delete"
//            override val description = "Delete an user"
//            override val key: PermissionKey
//                get() = TODO("Not yet implemented")
//        }
//
//
//        override val permissions: Set<Permission> = setOf(
//            Create, View, Update, Delete
//        )
//    }
//
//    override val categories: Set<Category> = setOf(
//        Users,
//    )
//}

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

    object Admin : CategoryBase("admin", "Admin permissions", CoreDefinition) {
        val All = permission("all", "Does everything company level")
    }

    override val categories = setOf(Users, Admin)
}

val aux = CoreDefinition.Users.Create.key.toString()