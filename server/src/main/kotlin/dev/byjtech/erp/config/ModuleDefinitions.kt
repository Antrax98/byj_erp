package dev.byjtech.erp.config

import dev.byjtech.erp.core.permissions.ModulePermission
import io.ktor.server.routing.Route
import org.jetbrains.exposed.sql.Table

interface ModuleDefinition {
    val name: String
    val version: String // ejemplo "v1" "v2"
    val categories: List<ModuleCategory>?
    val tables: List<Table>?

    fun installRoutes(route: Route)
}

data class ModuleCategory(
    val name: String,
    val description: String,
    val permissions: List<ModulePermission>
)

//data class ModulePermission(
//    val action: String,
//    val description: String,
//    val category: String,
//    val module: String,
//    val version: String
//) {
//    val key: PermissionKey get() = PermissionKey(module, version, category, action)
//}
//
//data class PermissionKey(
//    val module: String,
//    val version: String,
//    val category: String,
//    val action: String
//) {
//    override fun toString(): String {
//        return "$module:$version:$category:$action"
//    }
//}