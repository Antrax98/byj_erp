package dev.byjtech.erp.common

import kotlinx.coroutines.flow.StateFlow


//usar en TODOS los componentes que sean navegables desde el Root de un modulo
interface PermissionAwareComponent {
    val requiredPermissions: Set<PermissionKey>
        get() = emptySet()
    val optionalPermissions: Set<PermissionKey>
        get() = emptySet()
    val userPermissions: StateFlow<Set<PermissionKey>>
}