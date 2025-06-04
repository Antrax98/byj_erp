package dev.byjtech.erp.common

import androidx.compose.runtime.Composable
import dev.byjtech.erp.core.dto.ModuleDTO

class ModuleManager(
    private val moduleEntries: Set<ModuleEntry>
) {
    //obtener mapa de mapas de botones de features, mapa con nombre de modulos y su valor es un mapa con nombre de features y su valor es un ¿boton?
    //este es necesario para el FeatureListComponent para mostrar los botones de los features y navegar a estos
    fun buttonMap(userPermissions: Set<PermissionKey>, permittedModules: Set<ModuleDTO>): Map<String,Map<String, ButtonMetadata>> {
        val permittedModuleNames = permittedModules.map { it.name }.toSet()
        return moduleEntries
            .filter { it.name in permittedModuleNames }
            .associate { moduleEntry ->
                val permittedFeatures = moduleEntry.features
                    .filter { featureEntry ->
                        userPermissions.containsAll(featureEntry.requiredPermissions)
                    }
                    .associate { featureEntry ->
                        featureEntry.name to featureEntry.buttonMetadata
                    }
                moduleEntry.name to permittedFeatures
            }
            .filterValues { it.isNotEmpty() }
    }

    //obtener mapa de mapas de features, mapa con nombre de modulos y su valor es un mapa con nombre de features y su valor es un featureComponentFactory
    //este es necesario para el ChildFactory del HomeScreen
    fun featureFactoryMap(userPermissions: Set<PermissionKey>, permittedModules: Set<ModuleDTO>): Map<String, Map<String, FeatureComponentFactory>> {
        val permittedModuleNames = permittedModules.map { it.name }.toSet()
        return moduleEntries
            .filter { it.name in permittedModuleNames }
            .associate { moduleEntry ->
                val permittedFeatures = moduleEntry.features
                    .filter { featureEntry ->
                        userPermissions.containsAll(featureEntry.requiredPermissions)
                    }
                    .associate { featureEntry -> featureEntry.name to featureEntry.factory }
                moduleEntry.name to permittedFeatures
            }
            .filterValues { it.isNotEmpty() }
    }

    //obtener mapa de mapas de screens, mapa con nombre de modulos y su valor es un mapa con nombre de features y su valor es un screen del feature
    //este es necesario para el HomeScreen
//    fun screenMap(userPermissions: Set<PermissionKey>, permittedModules: Set<ModuleDTO>): Map<String, Map<String, @Composable (FeatureComponent) -> Unit>> {
//        val permittedModuleNames = permittedModules.map { it.name }.toSet()
//
//        return moduleEntries
//            .filter { it.name in permittedModuleNames }
//            .associate { moduleEntry ->
//                val permittedFeaturesScreens = moduleEntry.features
//                    .filter { featureEntry ->
//                        userPermissions.containsAll(featureEntry.requiredPermissions)
//                    }
//                    .associate { featureEntry ->
//                        featureEntry.name to featureEntry.screen
//                    }
//                moduleEntry.name to permittedFeaturesScreens
//            }
//            .filterValues { it.isNotEmpty() }
//    }

    //la misma funcion pero sin filtrar por permisos y modulos
    fun screenMap(): Map<String, Map<String, @Composable (FeatureComponent) -> Unit>> =
        moduleEntries.associate { moduleEntry ->
            moduleEntry.name to moduleEntry.features.associate { featureEntry ->
                featureEntry.name to featureEntry.screen
            }
        }


    //porsiacaso
    fun featureEntryMap(userPermissions: Set<PermissionKey>, permittedModules: Set<ModuleDTO>): Map<String, Map<String, FeatureEntry>> {
        val permittedModuleNames = permittedModules.map { it.name }.toSet()

        return moduleEntries
            .filter { it.name in permittedModuleNames }
            .associate { moduleEntry ->
                val permittedFeatures = moduleEntry.features
                    .filter { featureEntry ->
                        userPermissions.containsAll(featureEntry.requiredPermissions)
                    }.associateBy { featureEntry -> featureEntry.name }
                moduleEntry.name to permittedFeatures
            }
            .filterValues { it.isNotEmpty() }
    }

}