package dev.byjtech.erp.core.response

import dev.byjtech.erp.core.dto.RoleDTO
import kotlinx.serialization.Serializable

@Serializable
data class UserRolesResponse(
    val roles: List<RoleDTO>
)
