package dev.byjtech.erp.core.response

import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.PermissionWithKey
import kotlinx.serialization.Serializable

@Serializable
data class RolePermisisonKeysResponse(
    val permissions: Set<PermissionWithKey>
)
