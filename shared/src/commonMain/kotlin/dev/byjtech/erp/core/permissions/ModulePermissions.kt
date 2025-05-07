package dev.byjtech.erp.core.permissions

data class ModulePermission(
    val action: String,
    val description: String,
    val module: String,
    val version: String,
    val category: String
) {
    val key: PermissionKey
        get() = PermissionKey(module, version, category, action)
}

data class PermissionKey(
    val module: String,
    val version: String,
    val category: String,
    val action: String
) {
    override fun toString(): String = "$module:$version:$category:$action"
}