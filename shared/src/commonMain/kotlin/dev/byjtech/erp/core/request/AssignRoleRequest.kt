package dev.byjtech.erp.core.request

import kotlinx.serialization.Serializable

@Serializable
data class AssignRoleRequest(
    val userId: Int,
    val roleId: Int
)
