package dev.byjtech.erp.modules.machinery.featureEntry

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.ui.graphics.Color
import dev.byjtech.erp.common.ButtonMetadata
import dev.byjtech.erp.common.ComponentConfig
import dev.byjtech.erp.common.FeatureEntry
import dev.byjtech.erp.modules.machinery.MachineryDefinition
import dev.byjtech.erp.modules.machinery.main.MachineryMainComponentImpl
import dev.byjtech.erp.modules.machinery.main.MachineryMainScreen

val machineryListFeatureEntry: FeatureEntry = FeatureEntry(
    name = "machineryMain",
    requiredAnyPermissions = setOf(
        MachineryDefinition.Machinery.View.key, // Para ver maquinarias
        MachineryDefinition.Machinery.Create.key // Para crear maquinarias
    ),
    factory = { context, userPermissions, apiClient, toHome, updateTitle ->
        MachineryMainComponentImpl(
            componentContext = context,
            userPermissions = userPermissions,
            apiClient = apiClient,
            toHome = toHome,
            updateTitle = updateTitle
        )
    },
    screen = { component -> MachineryMainScreen(component as dev.byjtech.erp.modules.machinery.main.MachineryMainComponent) },
    buttonMetadata = ButtonMetadata(
        displayName = "Maquinaria",
        color = Color(0xFF4CAF50),
        icon = Icons.Filled.Build,
        config = ComponentConfig(
            module = MachineryDefinition.name,
            feature = "machineryMain"
        )
    )
)
