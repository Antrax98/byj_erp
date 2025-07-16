package dev.byjtech.erp.document_management.features.documents

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.ui.graphics.Color
import dev.byjtech.erp.common.ButtonMetadata
import dev.byjtech.erp.common.ComponentConfig
import dev.byjtech.erp.common.FeatureEntry
import dev.byjtech.erp.document_management.DocumentManagementDefinition

val DocumentsFeatureEntry: FeatureEntry = FeatureEntry(
    name = "documents",
    requiredAnyPermissions = setOf(
        DocumentManagementDefinition.Documents.View.key,
        DocumentManagementDefinition.Documents.Create.key,
        DocumentManagementDefinition.Documents.Update.key,
        DocumentManagementDefinition.Documents.Delete.key
    ),
    factory = { context, userPermissions, apiClient, toHome, updateTitle ->
        DocumentsFeatureComponentImpl(
            componentContext = context,
            userPermissions = userPermissions,
            apiClient = apiClient,
            toHome = toHome,
            updateTitle = updateTitle
        )
    },
    screen = { component -> DocumentsFeatureScreen(component as DocumentsFeatureComponent) },
    buttonMetadata = ButtonMetadata(
        displayName = "Documentos",
        color = Color(0xFFE3F2FD),
        icon = Icons.Filled.Description,
        config = ComponentConfig(
            module = DocumentManagementDefinition.name,
            feature = "documents"
        )
    )
)
