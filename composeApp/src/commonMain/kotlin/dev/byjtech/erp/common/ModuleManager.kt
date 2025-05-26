package dev.byjtech.erp.common

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext

class ModuleManager(
    private val modules: List<ModuleEntry> // injectarlo desde el archivo Koin
) {
//    fun loadModules(parentContext: ComponentContext, toHome: () -> Unit): List<ModuleRootComponent> =
//        modules.map { (name, factory) -> factory.create(parentContext.childContext(name), toHome) }

    fun metadataMap(): Map<String, ModuleMetadata> =
        modules.associate { it.name to it.metadata }

//    fun moduleAndMetadataMap(
//        parentContext: ComponentContext,
//        toHome: () -> Unit
//    ): Map<String, Pair<ModuleRootComponent, ModuleMetadata>> =
//        modules.associate { (name, factory, metadata) ->
//            name to (factory.create(parentContext.childContext(name), toHome) to metadata)
//        }

    fun factoryAndMetadataMap(): Map<String, Pair<ModuleRootComponentFactory, ModuleMetadata>> =
        modules.associate { it.name to (it.factory to it.metadata) }

    fun entriesByName(): Map<String, ModuleEntry> =
        modules.associateBy { it.name }
}
