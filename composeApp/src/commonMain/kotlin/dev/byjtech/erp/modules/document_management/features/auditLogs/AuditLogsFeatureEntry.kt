package dev.byjtech.erp.modules.document_management.features.auditLogs

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.ui.graphics.Color
import dev.byjtech.erp.common.ButtonMetadata
import dev.byjtech.erp.common.ComponentConfig
import dev.byjtech.erp.common.FeatureEntry
import dev.byjtech.erp.document_management.DocumentManagementDefinition
import dev.byjtech.erp.common.UnderConstructionComponent

val AuditLogsFeatureEntry: FeatureEntry = FeatureEntry(
    name = "audit_logs",
    requiredAnyPermissions = setOf(
        DocumentManagementDefinition.AuditLogs.View.key
    ),
    factory = { context, userPermissions, apiClient, toHome, updateTitle ->
        UnderConstructionComponent(
            componentContext = context,
            userPermissions = userPermissions,
            apiClient = apiClient,
            toHome = toHome,
            updateTitle = updateTitle,
            featureName = "Bitácora de Auditoría"
        )
    },
    screen = { component -> 
        val underConstructionComponent = component as UnderConstructionComponent
        dev.byjtech.erp.common.UnderConstructionScreen(underConstructionComponent.featureName) 
    },
    buttonMetadata = ButtonMetadata(
        displayName = "Auditoría",
        color = Color(0xFFFFF3E0),
        icon = Icons.Filled.History,
        config = ComponentConfig(
            module = DocumentManagementDefinition.name,
            feature = "audit_logs"
        )
    )
)
