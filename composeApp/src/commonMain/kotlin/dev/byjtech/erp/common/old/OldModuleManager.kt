package dev.byjtech.erp.common.old

class OldModuleManager(
    private val modules: List<OldModuleEntry> // injectarlo desde el archivo Koin
) {
//    fun loadModules(parentContext: ComponentContext, toHome: () -> Unit): List<ModuleRootComponent> =
//        modules.map { (name, factory) -> factory.create(parentContext.childContext(name), toHome) }

    fun metadataMap(): Map<String, OldModuleMetadata> =
        modules.associate { it.name to it.metadata }

//    fun moduleAndMetadataMap(
//        parentContext: ComponentContext,
//        toHome: () -> Unit
//    ): Map<String, Pair<ModuleRootComponent, ModuleMetadata>> =
//        modules.associate { (name, factory, metadata) ->
//            name to (factory.create(parentContext.childContext(name), toHome) to metadata)
//        }

    fun factoryAndMetadataMap(): Map<String, Pair<OldModuleRootComponentFactory, OldModuleMetadata>> =
        modules.associate { it.name to (it.factory to it.metadata) }

    fun entriesByName(): Map<String, OldModuleEntry> =
        modules.associateBy { it.name }
}
