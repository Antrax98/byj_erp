package dev.byjtech.erp.common

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

data class ButtonMetadata(
    val displayName: String,
    val color: Color,
    val icon: ImageVector? = null, // sujeto a cambios (probablemente sea un ID de recurso de imagen)
    val config: ComponentConfig
)
