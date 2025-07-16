package dev.byjtech.erp.document_management.features.editHistory

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.ui.graphics.Color
import dev.byjtech.erp.common.ButtonMetadata
import dev.byjtech.erp.common.ComponentConfig
import dev.byjtech.erp.common.FeatureEntry
import dev.byjtech.erp.document_management.DocumentManagementDefinition
import dev.byjtech.erp.common.UnderConstructionComponent

val EditHistoryFeatureEntry: FeatureEntry = FeatureEntry(
    name = "edit_history",
    requiredAnyPermissions = setOf(
        DocumentManagementDefinition.EditHistory.View.key
    ),
    factory = { context, userPermissions, apiClient, toHome, updateTitle ->
        UnderConstructionComponent(
            componentContext = context,
            userPermissions = userPermissions,
            apiClient = apiClient,
            toHome = toHome,
            updateTitle = updateTitle,
            featureName = "Historial de Ediciones"
        )
    },
    screen = { component -> 
        val underConstructionComponent = component as dev.byjtech.erp.common.UnderConstructionComponent
        dev.byjtech.erp.common.UnderConstructionScreen(underConstructionComponent.featureName) 
    },
    buttonMetadata = ButtonMetadata(
        displayName = "Historial",
        color = Color(0xFFF3E5F5),
        icon = Icons.Filled.Edit,
        config = ComponentConfig(
            module = DocumentManagementDefinition.name,
            feature = "edit_history"
        )
    )
)
