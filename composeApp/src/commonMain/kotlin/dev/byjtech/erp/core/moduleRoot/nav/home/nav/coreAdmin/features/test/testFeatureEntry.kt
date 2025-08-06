package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.test

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.ui.graphics.Color
import dev.byjtech.erp.common.ButtonMetadata
import dev.byjtech.erp.common.ComponentConfig
import dev.byjtech.erp.common.FeatureEntry
import dev.byjtech.erp.core.CoreDefinition

//test para ver si funciona el featureEntry con el permiso de admin
val testFeatureEntry: FeatureEntry = FeatureEntry(
    name = "test",
    requiredAnyPermissions = setOf(
        CoreDefinition.Admin.All.key
    ),
    factory = { context, userPermissions, apiClient, toHome, updateTitle ->
        TestFeatureComponentImpl(
            componentContext = context,
            userPermissions = userPermissions,
            apiClient = apiClient,
            toHome = toHome,
            updateTitle = updateTitle
        )
    },
    screen = { component -> TestFeatureScreen(component as TestFeatureComponent) },
    buttonMetadata = ButtonMetadata(
        displayName = "Test",
        color = Color(0xFFB2E5BF),
        icon = Icons.Filled.Bolt,
        config = ComponentConfig(
            module = CoreDefinition.name,
            feature = "test"
        )
    ),
)
