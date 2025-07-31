package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.filled.Person4
import androidx.compose.ui.graphics.Color
import dev.byjtech.erp.common.ButtonMetadata
import dev.byjtech.erp.common.ComponentConfig
import dev.byjtech.erp.common.FeatureEntry
import dev.byjtech.erp.core.CoreDefinition

val UsersFeatureEntry: FeatureEntry = FeatureEntry(
    name = "users",
    requiredAnyPermissions = setOf(
        CoreDefinition.Admin.All.key,
        CoreDefinition.Users.View.key,
    ),
    factory = { context, userPermissions, apiClient, toHome, updateTitle ->
        UsersFeatureComponentImpl(
            componentContext = context,
            userPermissions = userPermissions,
            apiClient = apiClient,
            toHome = toHome,
            updateTitle = updateTitle,
            sessionManagerRef = null
        )
    },
    screen = { component -> UsersFeatureScreen(component as UsersFeatureComponent) },
    buttonMetadata = ButtonMetadata(
        displayName = "Users",
        color = Color(0xFFF4B3B3),
        icon = Icons.Filled.Person2,
        config = ComponentConfig(
            module = CoreDefinition.name,
            feature = "users"
        )
    )


)