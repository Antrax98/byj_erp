package dev.byjtech.erp.common

import androidx.compose.runtime.Composable

data class ModuleEntry(
    val name: String,
    val factory: ModuleRootComponentFactory,
    val renderScreen: @Composable (ModuleRootComponent) -> Unit,
    val metadata: ModuleMetadata
)