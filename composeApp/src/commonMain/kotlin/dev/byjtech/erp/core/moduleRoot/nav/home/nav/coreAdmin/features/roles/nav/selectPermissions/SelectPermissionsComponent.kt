package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.selectPermissions

import dev.byjtech.erp.common.PermissionAwareComponent
import dev.byjtech.erp.common.PermissionWithKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

interface SelectPermissionsComponent: PermissionAwareComponent {
    val permissions: StateFlow<List<PermissionWithKey>?> // Lista de permisos disponibles
    val ownedPermissions: MutableStateFlow<List<PermissionWithKey>?> // Lista de permisos ya en el Rol
    val selectedPermissions: MutableStateFlow<List<PermissionWithKey>?> // Lista de permisos seleccionados
    suspend fun fetchPermissions()

}