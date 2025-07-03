package dev.byjtech.erp.common

import dev.byjtech.erp.common.api.ApiClient
import kotlinx.coroutines.flow.StateFlow

//reemplaza a ModuleRootComponent
//este aparecera directamente en el home como un boton
//dependiendo de los permisos necesarios, el usuario lo vera o no
//(no darle permisos si se quiere que siempre sea visible)
interface FeatureComponent {
    val userPermissions: StateFlow<Set<PermissionKey>>
    val apiClient: ApiClient
    val toHome: () -> Unit //ELIMINAR EN UN FUTURO
    val updateTitle: (newTitle: String) -> Unit
    fun onBack(): Boolean //hace pop a su propia navegacion devolviendo true o si no pudo devuelve false
}