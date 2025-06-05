package dev.byjtech.erp.core.response

import dev.byjtech.erp.core.dto.UserDTO
import kotlinx.serialization.Serializable

@Serializable
data class CompanyUsersResponse(
    val users: List<UserDTO>
)
