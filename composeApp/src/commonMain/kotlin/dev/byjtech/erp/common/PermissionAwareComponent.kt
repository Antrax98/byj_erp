package dev.byjtech.erp.common

import dev.byjtech.erp.core.CoreDefinition
import kotlinx.coroutines.flow.StateFlow


//usar en TODOS los componentes que sean navegables desde el Feature de un modulo
//el permiso de admin no hay que darlo explicitamente ya que lo detecta automaticamente en requiredPermissions
//en opcionales hay que tomarlo en cuenta por cada caso
interface PermissionAwareComponent {
    val requiredPermissions: Set<PermissionKey>
        get() = emptySet()
    val optionalPermissions: Set<PermissionKey>
        get() = emptySet()
    val userPermissions: StateFlow<Set<PermissionKey>>
}

//automaticamente le da permiso al admin
fun PermissionAwareComponent.canAccessComponent(): Boolean {
    val admin = requiredPermissions.any { it == CoreDefinition.Admin.All.key }
    if (admin) return true
    return requiredPermissions.any { it in userPermissions.value }
}

//TODO: hacer que esto funcione, la idea es saber que permisos opcionales tiene el usuario para saber que opciones activar en el componente
//fun PermissionAwareComponent.accesibleOptional(): Set<PermissionKey> {
//    //val admin = requiredPermissions.any { it == CoreDefinition.Admin.All.key }
//    return optionalPermissions.filter { it in userPermissions.value }.toSet()
//}