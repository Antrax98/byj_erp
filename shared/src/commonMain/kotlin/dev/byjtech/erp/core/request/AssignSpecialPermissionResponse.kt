package dev.byjtech.erp.core.request

import dev.byjtech.erp.common.PermissionKey
import kotlinx.serialization.Serializable

@Serializable
data class AssignSpecialPermissionRequest(
    val userId: String,
    val permissionId: String?,
    val permissionKey: PermissionKey?
)
