package dev.byjtech.erp.core.response

import dev.byjtech.erp.common.PermissionKey
import kotlinx.serialization.Serializable

@Serializable
data class PermissionKeysResponse(
    val permissionKeys: List<PermissionKey>
)