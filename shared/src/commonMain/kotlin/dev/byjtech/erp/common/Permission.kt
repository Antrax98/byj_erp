package dev.byjtech.erp.common


interface Permission {
    val action: String
    val description: String
    val module: ModuleDefinition
    val category: Category
    val key: PermissionKey
        get() = PermissionKey(module.name, category.name, action)
}