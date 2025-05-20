package dev.byjtech.erp.shared.infrastructure.database

import dev.byjtech.erp.config.ModuleInitializer
import dev.byjtech.erp.core.infrastructure.exposed.entities.CategoryEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.ModuleEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.PermissionEntity
import dev.byjtech.erp.core.infrastructure.exposed.tables.ModulesTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import dev.byjtech.erp.core.infrastructure.exposed.tables.*

class DatabaseInitializer (private val database: Database) {

    //no muy usable por ahora
    fun initialize(vararg tables: Table) {
        transaction(database) {
            SchemaUtils.create(
                *tables
                //*CoreTables.all.toTypedArray()
            )
        }
    }

    //no muy usable por ahora
    fun createTables(vararg tables: Table) {
        transaction(database) {
            SchemaUtils.create(
                *tables
            )
        }
    }


    fun nuke() {
        transaction(database) {
//            SchemaUtils.drop(
//                *CoreTables.all.reversed().toTypedArray()
//            )
        }
    }

    fun multiCreate(
        modules: List<ModuleInitializer>,
        maxRounds: Int = 10
    ) {
        val remaining = modules.toMutableSet()
        var round = 0

        while (remaining.isNotEmpty() && round < maxRounds) {
            val iterator = remaining.iterator()
            var atLeastOneCreated = false

            while (iterator.hasNext()) {
                val module = iterator.next()
                try {
                    transaction(database) {
                        SchemaUtils.create(*module.tables.toTypedArray())
                    }
                    atLeastOneCreated = true
                    iterator.remove()
                    println("Tablas creadas del módulo: ${module.definition.name}")
                } catch (e: Exception) {
                    println("⚠Error al crear tablas de ${module.definition.name} en ronda $round: ${e.message}")
                    // sigue con el siguiente
                }
            }

            if (!atLeastOneCreated) {
                throw IllegalStateException("No se pudo crear ninguna tabla en la ronda $round. Verifica dependencias o errores.")
            }

            round++
        }

        if (remaining.isNotEmpty()) {
            throw IllegalStateException("No se pudieron crear todas las tablas después de $round intentos.")
        }

        println("Todas las tablas fueron creadas correctamente.")
    }

    //TODO() hacer que la funcion use un service de core para guardar los modulos y permisos
    //por ahora se hace a mano aqui directamente con el transaction y entities
    fun registerModuleDefinitions(modules: List<ModuleInitializer>) {
        modules.forEach { moduleInit ->
            transaction(database) {
                val def = moduleInit.definition
                val exist = ModuleEntity.find {
                    ModulesTable.name eq def.name

                }.firstOrNull()

                if (exist == null) {
                    val newModule = ModuleEntity.new {
                        name = def.name
                        displayName = def.displayName
                        description = def.description
                        developerOnly = def.developerOnly
                    }
                    println("Module ${def.name} created")
                    val categories = def.categories
                    categories.forEach { categoryAct ->
                        val newCat = CategoryEntity.new {
                            name = categoryAct.name
                            description = categoryAct.description
                            module = newModule
                        }
                        println("Category ${categoryAct.name} created")
                        categoryAct.permissions.forEach { permission ->
                            PermissionEntity.new {
                                name = permission.action
                                description = permission.description
                                category = newCat
                            }
                        }
                        val permissions = categoryAct.permissions
                        permissions.forEach { permission ->
                            PermissionEntity.new {
                                name = permission.action
                                description = permission.description
                                category = newCat
                            }
                            println("Permission ${permission.action} created")
                        }
                    }
                } else {
                    println("Module ${def.name} already exists")
                    println("si esto pasa y las categorias o permisos no existen, borrar lo relacionado con el modulo y reintentarlo")
                }

            }
        }
    }

}