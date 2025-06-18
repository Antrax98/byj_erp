package dev.byjtech.erp.shared.infrastructure.database

import dev.byjtech.erp.config.ModuleInitializer
import dev.byjtech.erp.core.infrastructure.exposed.entities.CategoryEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.CompanyEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.ModuleEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.PermissionEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.RoleEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.RolePermissionEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.SubscriptionEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.UserEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.UserRoleEntity
import dev.byjtech.erp.core.infrastructure.exposed.tables.ModulesTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.transactions.transaction
import dev.byjtech.erp.core.infrastructure.exposed.tables.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import java.util.UUID

class DatabaseInitializer (private val database: Database) {

//    fun multiCreate(
//        modules: List<ModuleInitializer>,
//        maxRounds: Int = 10
//    ) {
//        val remaining = modules.toMutableSet()
//        var round = 0
//
//        while (remaining.isNotEmpty() && round < maxRounds) {
//            val iterator = remaining.iterator()
//            var atLeastOneCreated = false
//
//            while (iterator.hasNext()) {
//                val module = iterator.next()
//                try {
//                    transaction(database) {
//                        SchemaUtils.create(*module.tables.toTypedArray())
//                    }
//                    atLeastOneCreated = true
//                    iterator.remove()
//                    println("Tablas creadas del módulo: ${module.definition.name}")
//                } catch (e: Exception) {
//                    println("Error al crear tablas de ${module.definition.name} en ronda $round: ${e.message}")
//                    // sigue con el siguiente
//                }
//            }
//
//            if (!atLeastOneCreated) {
//                throw IllegalStateException("No se pudo crear ninguna tabla en la ronda $round. Verifica dependencias o errores.")
//            }
//
//            round++
//        }
//
//        if (remaining.isNotEmpty()) {
//            throw IllegalStateException("No se pudieron crear todas las tablas después de $round intentos.")
//        }
//
//        println("Todas las tablas fueron creadas correctamente.")
//    }

    //TODO() hacer que la funcion use un service de core para guardar los modulos y permisos
    //por ahora se hace a mano aqui directamente con el transaction y entities
    fun registerModuleDefinitions(modules: List<ModuleInitializer>) {
        println("registrando modulos WWWWWWWWWWWWWWWWWW")
        println(modules)
        modules.forEach { moduleInit ->
            try {
                transaction(database) {
                    val def = moduleInit.definition
                    println("Checking if module exists: '${def.name}'")
                    val exist = ModuleEntity.find {
                        ModulesTable.name eq def.name
                    }.firstOrNull()
                    println("Exist result: $exist")

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
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    //esto esta hecho para que solo cree una compañia, si ya existe al menos una no hace nada
    fun firstDataInitialization(){
        transaction(database) {
            val exist = CompanyEntity.all().firstOrNull()
            if (exist != null) {
                return@transaction
            }
            //compañia
            val newcompany = CompanyEntity.new(UUID.randomUUID()) {
                name = "Byjtech"
                contactEmail = "test@byjtech.com"
            }

            //usuarios
            val newUser1 = UserEntity.new(UUID.randomUUID()) {
                email = "kazapox@gmail.com"
                company = newcompany
            }
            val newUser2 = UserEntity.new(UUID.randomUUID()) {
                email = "s.sanhuezasalas@gmail.com"
                company = newcompany
            }

            //roles
            //cada vez que se crea una compañia se crea un rol admin (y se añade un user al que se le asigna)
            //hacer que la palabra admin sea un nombre reservado para que no se puedan crear mas roles admin (o modificarlo?)
            val adminRole = RoleEntity.new(UUID.randomUUID()) {
                name = "admin"
                description = "admin role"
                company = newcompany
            }

            //asignar permisos a roles
            //todos los modulos deverian tener una categoria admin con un permiso "all", para entrar a tod.o lo relacionado con el modulo
            //el codigo siguiente se asegura de darle el permiso all al rol admin del core especificamente
            //al momento de subscribir modulos a las compañias, se le asignara el permiso all al rol admin del modulo, (manualmente o automatico)
            val coreModule = ModuleEntity.find(ModulesTable.name eq "core").firstOrNull()
//            val adminPermission = PermissionEntity.find((PermissionsTable.name eq "all")and (PermissionsTable.categoryId eq CategoryEntity.find((CategoriesTable.name eq "admin")and (CategoriesTable.moduleId eq coreModule?.id?.value)).firstOrNull()?.id?.value)).firstOrNull()
//                ?: throw Exception("No se encontro el permiso all para admin en modulo core")
            // Paso 1: Obtener el módulo core
            val moduleId = coreModule?.id?.value
            if (moduleId == null) throw Exception("coreModule es null")

            // Paso 2: Buscar la categoría "admin" del módulo core
            val adminCategory = CategoryEntity
                .find((CategoriesTable.name eq "admin") and (CategoriesTable.moduleId eq moduleId))
                .firstOrNull()
            if (adminCategory == null) throw Exception("No se encontró la categoría 'admin' en el módulo core")

            // Paso 3: Buscar el permiso "all" dentro de esa categoría
            val adminPermission = PermissionEntity
                .find((PermissionsTable.name eq "all") and (PermissionsTable.categoryId eq adminCategory.id.value))
                .firstOrNull()
            if (adminPermission == null) throw Exception("No se encontró el permiso 'all' para la categoría 'admin' en el módulo core")

            RolePermissionEntity.new(UUID.randomUUID()) {
                role = adminRole
                permission = adminPermission
            }

            //asignar role
            UserRoleEntity.new(UUID.randomUUID()) {
                user = newUser1
                role = adminRole
            }

            //crear subscripcion a core
            SubscriptionEntity.new(UUID.randomUUID()) {
                company = newcompany
                module = ModuleEntity.find(ModulesTable.name eq "core").firstOrNull()
                    ?: throw Exception("No se encontro el modulo core")
            }


        }
    }

}