package dev.byjtech.erp.common

import dev.byjtech.erp.common.api.ApiClient
import kotlinx.coroutines.flow.StateFlow

//reemplaza a ModuleRootComponent
//este aparecera directamente en el home como un boton
//dependiendo de los permisos necesarios, el usuario lo vera o no
//(no darle permisos si se quiere que siempre sea visible)
interface FeatureComponent {
    //val name: String
    val userPermissions: StateFlow<Set<PermissionKey>>
    val apiClient: ApiClient
    val toHome: () -> Unit
}