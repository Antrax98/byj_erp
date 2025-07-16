package dev.byjtech.erp.shared.infrastructure.database

import dev.byjtech.erp.config.ModuleInitializer
import dev.byjtech.erp.core.infrastructure.exposed.entities.CategoryEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.CompanyEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.ModuleEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.PermissionEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.RoleEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.RolePermissionEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.SubscriptionEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.SuperAdminEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.UserEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.UserPermissionEntity
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

//por ahora SOLO es utilisable por el Core ya que usa tablas unicas de este
class DatabaseInitializer (private val database: Database) {

    //TODO() hacer que la funcion use un service de core para guardar los modulos y permisos
    //por ahora se hace a mano aqui directamente con el transaction y entities
    fun registerModuleDefinitions(modules: List<ModuleInitializer>) {
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
                                    // TODO: Comentado temporalmente por error de compilación con campo 'category'
                                    // category = newCat
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
    //y especificamente solo para tests
    fun firstDataInitialization(){
        transaction(database) {

            val admins = setOf(
                "usuariotesttesttester@gmail.com",
                "minepoker.lol@gmail.com",
                "anaysmr21@gmail.com"
            )

            admins.forEach { auxemail ->
                val exist = UserEntity.find { UsersTable.email eq auxemail }.firstOrNull()
                if (exist != null) {
                    return@forEach
                }
                val superAdmin = UserEntity.new(UUID.randomUUID()) {
                    email = auxemail
                }
                SuperAdminEntity.new(UUID.randomUUID()) {
                    user = superAdmin
                    description = "suepradmin test"
                }
            }


        }
        transaction(database) {
            val exist = CompanyEntity.all().firstOrNull()
            if (exist != null) {
                return@transaction
            }

            //compañia
            val newcompany = CompanyEntity.new(UUID.randomUUID()) {
                name = "Byjtech"
                contactEmail = "test@byjtech.com"
                rut = "77.777.777-7"
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


            //asignar permisos
            //el codigo siguiente se asegura de darle el permiso all al rol admin del core especificamente
            //al momento de subscribir modulos a las compañias, se le asignara el permiso all al rol admin del modulo, (manualmente o automatico)
            val coreModule = ModuleEntity.find(ModulesTable.name eq "core").firstOrNull()
            //Obtener el módulo core
            val moduleId = coreModule?.id?.value
            if (moduleId == null) throw Exception("coreModule es null")

            //Buscar la categoría "admin" del módulo core
            val adminCategory = CategoryEntity
                .find((CategoriesTable.name eq "admin") and (CategoriesTable.moduleId eq moduleId))
                .firstOrNull()
            if (adminCategory == null) throw Exception("No se encontró la categoría 'admin' en el módulo core")

            //Buscar el permiso "all" 
            val adminPermission = PermissionEntity
                .find(PermissionsTable.name eq "all")
                .firstOrNull()
            if (adminPermission == null) throw Exception("No se encontró el permiso 'all'")


            //asignar permiso especial
            UserPermissionEntity.new {
                user = newUser1
                permission = adminPermission
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