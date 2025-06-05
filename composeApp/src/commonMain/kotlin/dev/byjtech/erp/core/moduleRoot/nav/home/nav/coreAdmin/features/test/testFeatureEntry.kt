package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.test

import dev.byjtech.erp.common.ButtonMetadata
import dev.byjtech.erp.common.ComponentConfig
import dev.byjtech.erp.common.FeatureEntry
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.api.users.UsersTenantApi
import dev.byjtech.erp.core.api.users.createUsersTenantApi

//test para ver si funciona el featureEntry con el permiso de admin
val testFeatureEntry: FeatureEntry = FeatureEntry(
    name = "test",
    requiredAnyPermissions = setOf(
        CoreDefinition.Admin.All.key
    ),
    factory = { context, userPermissions, apiClient, toHome ->
        TestFeatureComponentImpl(
            componentContext = context,
            userPermissions = userPermissions,
            apiClient = apiClient,
            toHome = toHome,
            usersTenantApi = apiClient.ktorfit.createUsersTenantApi(), //solo para testear si funciona este enfoque
        )
    },
    screen = { component -> TestFeatureScreen(component as TestFeatureComponent) },
    buttonMetadata = ButtonMetadata(
        displayName = "Test",
        color = "#32a852",
        icon = "#32a852",
        config = ComponentConfig(
            module = CoreDefinition.name,
            feature = "test"
        )
    ),
)
