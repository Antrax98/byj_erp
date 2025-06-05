package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users

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
    factory = { context, userPermissions, apiClient, toHome ->
        UsersFeatureComponentImpl(
            componentContext = context,
            userPermissions = userPermissions,
            apiClient = apiClient,
            toHome = toHome
        )
    },
    screen = { component -> UsersFeatureScreen(component as UsersFeatureComponent) },
    buttonMetadata = ButtonMetadata(
        displayName = "Users",
        color = "#32a852",
        icon = "#32a852",
        config = ComponentConfig(
            module = CoreDefinition.name,
            feature = "users"
        )
    )


)