package dev.byjtech.erp.core.request

import dev.byjtech.erp.common.PermissionKey
import kotlinx.serialization.Serializable

@Serializable
data class AssignPermissionRoleRequest(
    val roleId: String,
    val permissionIds: Set<String>? = null,
    val permissionKeys: Set<PermissionKey>? = null
)
