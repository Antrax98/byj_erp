package dev.byjtech.erp.common.old

import androidx.compose.runtime.Composable

data class OldModuleEntry(
    val name: String,
    val factory: OldModuleRootComponentFactory,
    val renderScreen: @Composable (OldModuleRootComponent) -> Unit,
    val metadata: OldModuleMetadata
)