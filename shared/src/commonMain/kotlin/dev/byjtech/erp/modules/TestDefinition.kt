package dev.byjtech.erp.modules

import dev.byjtech.erp.common.CategoryBase
import dev.byjtech.erp.common.ModuleDefinition

object TestDefinition : ModuleDefinition {
    override val name = "test"
    override val displayName = "Test"
    override val description = "Test module"
    override val developerOnly = false

    object Languages : CategoryBase("languages", "Language management", TestDefinition) {
        val Create = permission("create", "Create a new language")
        val View   = permission("view", "View a language")
        val Update = permission("update", "Update a language")
        val Delete = permission("delete", "Delete a language")
    }

    override val categories = setOf(Languages)
}