package dev.byjtech.erp.common

interface ModuleDefinition {
    val name: String
    val displayName: String
    val description: String
    val developerOnly: Boolean
    val categories: Set<Category>
}