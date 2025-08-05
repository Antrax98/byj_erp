package dev.byjtech.erp.modules.machinery.featureEntry

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.ui.graphics.Color
import dev.byjtech.erp.common.ButtonMetadata
import dev.byjtech.erp.common.ComponentConfig
import dev.byjtech.erp.common.FeatureEntry
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.modules.machinery.MachineryDefinition
import dev.byjtech.erp.modules.machinery.list.MachineryListComponentImpl
import dev.byjtech.erp.modules.machinery.list.MachineryListScreen

val machineryListFeatureEntry: FeatureEntry = FeatureEntry(
    name = "machineryList",
    requiredAnyPermissions = setOf(
        MachineryDefinition.Machinery.View.key // Usando permiso específico de machinery
    ),
    factory = { context, userPermissions, apiClient, toHome, updateTitle ->
        MachineryListComponentImpl(
            componentContext = context,
            userPermissions = userPermissions,
            apiClient = apiClient,
            toHome = toHome,
            updateTitle = updateTitle
        )
    },
    screen = { component -> MachineryListScreen(component as dev.byjtech.erp.modules.machinery.list.MachineryListComponent) },
    buttonMetadata = ButtonMetadata(
        displayName = "Maquinaria",
        color = Color(0xFF4CAF50),
        icon = Icons.Filled.Build,
        config = ComponentConfig(
            module = MachineryDefinition.name,
            feature = "machineryList"
        )
    )
)
