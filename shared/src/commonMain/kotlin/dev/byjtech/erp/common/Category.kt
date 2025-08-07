package dev.byjtech.erp.common

interface Category {
    val name: String
    val description: String
    val module: ModuleDefinition
    val permissions: Set<Permission>
}