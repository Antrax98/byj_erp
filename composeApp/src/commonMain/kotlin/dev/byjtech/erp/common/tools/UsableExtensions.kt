package dev.byjtech.erp.common.tools

import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.core.CoreDefinition

fun Set<PermissionKey>.containsAnyOf(other: Collection<PermissionKey>): Boolean =
    CoreDefinition.Admin.All.key in this || other.any { it in this }

fun Set<PermissionKey>.containsAnyOf(vararg keys: PermissionKey): Boolean =
    CoreDefinition.Admin.All.key in this || keys.any { it in this }

