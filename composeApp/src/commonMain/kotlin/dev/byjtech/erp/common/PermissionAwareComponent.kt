package dev.byjtech.erp.common


//usar en TODOS los componentes que sean navegables desde el Root de un modulo
interface PermissionAwareComponent {
    val requiredPermissions: Set<PermissionKey>
    val optionalPermissions: Set<PermissionKey>
}