package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Task
import androidx.compose.ui.graphics.Color
import dev.byjtech.erp.common.ButtonMetadata
import dev.byjtech.erp.common.ComponentConfig
import dev.byjtech.erp.common.FeatureEntry
import dev.byjtech.erp.core.CoreDefinition

val RolesFeatureEntry: FeatureEntry = FeatureEntry(
    name = "roles",
    requiredAnyPermissions = setOf(
        CoreDefinition.Admin.All.key
    ),
    factory = { context, userPermissions, apiClient, toHome, updateTitle ->
        RolesFeatureComponentImpl(
            componentContext = context,
            userPermissions = userPermissions,
            apiClient = apiClient,
            toHome = toHome,
            sessionManagerRef = null, //no se usa este factory por esta situacion solamente
            updateTitle = updateTitle
        )
    },
    screen = { component -> RolesFeatureScreen(component as RolesFeatureComponent) },
    buttonMetadata = ButtonMetadata(
        displayName = "Roles",
        color = Color(0xFFD1C4E9),
        icon = Icons.Filled.Task,
        config = ComponentConfig(
            module = CoreDefinition.name,
            feature = "roles"
        )
    )
)