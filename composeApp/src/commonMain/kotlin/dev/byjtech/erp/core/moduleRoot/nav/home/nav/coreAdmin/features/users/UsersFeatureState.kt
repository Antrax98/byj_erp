package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users

import dev.byjtech.erp.core.dto.UserDTO

data class UsersFeatureState(
    val isLoading: Boolean = false,
    val users: Set<UserDTO> = emptySet(),
    val error: String? = null
)
