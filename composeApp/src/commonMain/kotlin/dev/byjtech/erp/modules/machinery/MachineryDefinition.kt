package dev.byjtech.erp.modules.machinery

import dev.byjtech.erp.common.PermissionKey

object MachineryDefinition {
    const val name = "machinery"
    const val displayName = "Maquinaria"
    const val description = "Gestión de maquinaria y equipos"
    
    object Machinery {
        object View {
            val key = PermissionKey("machinery", "machinery", "view")
            const val description = "Ver maquinaria"
        }
        
        object Create {
            val key = PermissionKey("machinery", "machinery", "create")
            const val description = "Crear maquinaria"
        }
        
        object Update {
            val key = PermissionKey("machinery", "machinery", "update")
            const val description = "Actualizar maquinaria"
        }
        
        object Delete {
            val key = PermissionKey("machinery", "machinery", "delete")
            const val description = "Eliminar maquinaria"
        }
        
        object All {
            val key = PermissionKey("machinery", "machinery", "all")
            const val description = "Administrar maquinaria"
        }
    }
}
