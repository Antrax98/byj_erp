package dev.byjtech.erp.common

import androidx.compose.runtime.Composable

data class FeatureEntry(
    val name: String, //nombre del feature, unico en el modulo
    val requiredPermissions: Set<PermissionKey>, //permisos que requiere el feature
    val factory: FeatureComponentFactory, //factory que crea el feature
    val screen: @Composable (FeatureComponent) -> Unit, //screen que muestra el feature
    val buttonMetadata: ButtonMetadata //futuros datos para el boton del feature
)
