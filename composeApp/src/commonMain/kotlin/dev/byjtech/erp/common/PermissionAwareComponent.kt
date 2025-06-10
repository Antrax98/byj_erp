package dev.byjtech.erp.common

import dev.byjtech.erp.core.CoreDefinition
import kotlinx.coroutines.flow.StateFlow


//usar en TODOS los componentes que sean navegables desde el Feature de un modulo
interface PermissionAwareComponent {
    val requiredPermissions: Set<PermissionKey>
        get() = emptySet()
    val optionalPermissions: Set<PermissionKey>
        get() = emptySet()
    val userPermissions: StateFlow<Set<PermissionKey>>
}

fun PermissionAwareComponent.canAccessComponent(): Boolean {
    val admin = requiredPermissions.any { it == CoreDefinition.Admin.All.key }
    if (admin) return true
    return requiredPermissions.any { it in userPermissions.value }
}

//fun PermissionAwareComponent.accesibleOptional(): Set<PermissionKey> {
//    //val admin = requiredPermissions.any { it == CoreDefinition.Admin.All.key }
//    return optionalPermissions.filter { it in userPermissions.value }.toSet()
//}