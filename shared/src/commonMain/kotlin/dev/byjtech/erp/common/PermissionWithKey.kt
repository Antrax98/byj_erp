package dev.byjtech.erp.common

import kotlinx.serialization.Serializable

@Serializable
data class PermissionWithKey(
    val permission: Permission,
    val key: PermissionKey
)
