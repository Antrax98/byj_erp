package dev.byjtech.erp.common

import androidx.compose.runtime.Composable
import dev.byjtech.erp.common.old.OldModuleMetadata
import dev.byjtech.erp.common.old.OldModuleRootComponent
import dev.byjtech.erp.common.old.OldModuleRootComponentFactory

data class ModuleEntry(
    val name: String, //nombre del modulo
    val features: Set<FeatureEntry>, //set de features entry del modulo
)
