package dev.byjtech.erp.common

import dev.byjtech.erp.core.dto.PermissionDTO
import kotlinx.serialization.Serializable

@Serializable
data class PermissionWithKey(
    val permission: PermissionDTO,
    val key: PermissionKey
)
