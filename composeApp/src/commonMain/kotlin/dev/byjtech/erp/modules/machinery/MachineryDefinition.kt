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
        
        object All {
            val key = PermissionKey("machinery", "machinery", "all")
            const val description = "Administrar maquinaria"
        }
    }
}
