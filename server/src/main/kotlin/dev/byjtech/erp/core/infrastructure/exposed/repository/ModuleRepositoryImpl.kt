package dev.byjtech.erp.core.infrastructure.exposed.repository

import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.PermissionWithKey
import dev.byjtech.erp.core.domain.model.Category
import dev.byjtech.erp.core.domain.model.Permission
import dev.byjtech.erp.core.domain.repository.ModuleRepository
import dev.byjtech.erp.core.dto.CategoryDTO
import dev.byjtech.erp.core.dto.PermissionDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.CategoryEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.ModuleEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.PermissionEntity
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toModel
import dev.byjtech.erp.core.infrastructure.exposed.tables.CategoriesTable
import dev.byjtech.erp.core.infrastructure.exposed.tables.ModulesTable
import dev.byjtech.erp.core.infrastructure.exposed.tables.PermissionsTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID
import dev.byjtech.erp.core.domain.model.Module
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toDTO

class ModuleRepositoryImpl(private val db: Database): ModuleRepository {
    override fun create(module: Module): Module {
        TODO("Not yet implemented")
    }

    override fun get(moduleId: UUID): Module? {
        return transaction(db) {
            ModuleEntity.findById(moduleId)?.toModel()
        }
    }

    override fun getWithCategories(moduleId: UUID): Module? {
        TODO("Not yet implemented")
    }

    override fun getWithCategoriesAndPermissions(moduleId: UUID): Module? {
        TODO("Not yet implemented")
    }

    override fun getCategoryById(categoryId: UUID): Category? {
        TODO("Not yet implemented")
    }

    override fun getPermissionById(permissionId: UUID): Permission? {
        TODO("Not yet implemented")
    }

    override fun delete(moduleId: UUID) {
        TODO("Not yet implemented")
    }

    override fun getCategoryByPermissionId(permissionId: UUID): Category? {
        TODO("Not yet implemented")
    }

    override fun getByCategoryId(categoryId: UUID): Module? {
        TODO("Not yet implemented")
    }

    override fun getByPermissionId(permissionId: UUID): Module? {
        TODO("Not yet implemented")
    }

    override fun addCategory(moduleId: UUID, newCategory: CategoryDTO): Category {
        TODO("Not yet implemented")
    }

    override fun addPermissionToCategory(
        categoryId: UUID,
        newPermission: PermissionDTO
    ): Permission {
        TODO("Not yet implemented")
    }

    override fun findPermissionByPermissionKey(permissionKey: PermissionKey): Permission? {
        return transaction(db) {
            val module = ModuleEntity.find { ModulesTable.name eq permissionKey.module }.firstOrNull()
            if(module != null) {
                val category = CategoryEntity.find { (CategoriesTable.name eq permissionKey.category) and (CategoriesTable.moduleId eq module.id) }.firstOrNull()
                if(category != null) {
                    val permission = PermissionEntity.find { (PermissionsTable.name eq permissionKey.action) and (PermissionsTable.categoryId eq category.id) }.firstOrNull()
                    if(permission != null) {
                        return@transaction permission.toModel()
                    } else {
                        return@transaction null
                    }
                } else {
                    return@transaction null

                }
            } else {
                return@transaction null
            }
        }
    }

    //TODO: mejorarlo para que no haga tantas consultas a la base de datos
    //mapear los permissionKey de forma no repetida y por cada dato hacer una busqueda
    //ej: por cada moduleName se busca su modulo y por cada uno de sus categoryName se busca su category y asi hasta añadir el permission al set
    override fun findPermissionsByPermissionKeySet(permissionKeySet: Set<PermissionKey>): Set<Permission> {
        return transaction(db) {
            val permissions = mutableSetOf<Permission>()
            permissionKeySet.forEach { permissionKey ->
                findPermissionByPermissionKey(permissionKey)?.let {permissions.add(it) }
            }
            return@transaction permissions.toSet()
        }
    }

    override fun getPermissionKeysByPermissionIdSet(permissionIdSet: Set<UUID>): Set<PermissionKey> {
        if (permissionIdSet.isEmpty()) return emptySet()

        return transaction(db) {
            PermissionEntity.find { PermissionsTable.id inList permissionIdSet }
                .map { perm ->
                    PermissionKey(
                        module = perm.category.module.name,
                        category = perm.category.name,
                        action = perm.name
                    )
                }.toSet()
        }
    }

    override fun getPermissionKeyById(permissionId: UUID): PermissionKey? {
        return transaction(db) {
            val permission = PermissionEntity.findById(permissionId)
            if (permission != null) {
                PermissionKey(
                    module = permission.category.module.name,
                    category = permission.category.name,
                    action = permission.name
                )
            } else{
                null
            }
        }
    }

    override fun getPermissionKeysByModuleId(moduleId: UUID): Set<PermissionKey> {
        return transaction(db) {
            val permissionKeys = mutableSetOf<PermissionKey>()
            val module = ModuleEntity.findById(moduleId)
            if (module != null) {
                val categories = CategoryEntity.find { CategoriesTable.moduleId eq moduleId }
                categories.forEach { category ->
                    val permissions = PermissionEntity.find { PermissionsTable.categoryId eq category.id }
                    permissions.forEach { permission ->
                        permissionKeys.add(
                            PermissionKey(
                                module = module.name,
                                category = category.name,
                                action = permission.name
                            )
                        )
                    }
                }

            }
            return@transaction permissionKeys.toSet()
        }

    }

    override fun getPermissionsByModuleId(moduleId: UUID): Set<Permission> {
        return transaction(db) {
            val permissions = mutableSetOf<Permission>()
            val module = ModuleEntity.findById(moduleId)
            if (module != null) {
                val categories = CategoryEntity.find { CategoriesTable.moduleId eq moduleId }
                categories.forEach { category ->
                    val permissionsCategory =
                        PermissionEntity.find { PermissionsTable.categoryId eq category.id }
                    permissionsCategory.forEach { permission ->
                        permissions.add(permission.toModel())
                    }
                }
            }
            return@transaction permissions.toSet()
        }
    }

    override fun getPermissionsWithKeysByModuleIds(moduleIds: Set<UUID>): Set<PermissionWithKey> {
        return transaction(db) {
            val result = mutableSetOf<PermissionWithKey>()
            val modules = ModuleEntity.find { ModulesTable.id inList moduleIds.toList() }.associateBy { it.id.value }
            if (modules.isEmpty()) return@transaction emptySet()
            val categories = CategoryEntity.find { CategoriesTable.moduleId inList moduleIds.toList() }
            categories.forEach { category ->
                val module = modules[category.module.id.value] ?: return@forEach
                val permissions = PermissionEntity.find { PermissionsTable.categoryId eq category.id }
                permissions.forEach { permission ->
                    result.add(
                        PermissionWithKey(
                            permission = permission.toDTO(),
                            key = PermissionKey(
                                module = module.name,
                                category = category.name,
                                action = permission.name
                            )
                        )
                    )
                }
            }
            result
        }
    }

    //esto deveria ser un Service, pero hacerlo aqui genera menos consultas a la base de datos
    override fun getPermissionsWithKeysByPermissionIds(permissionIds: Set<UUID>): Set<PermissionWithKey> {
        return transaction(db) {
            val result = mutableSetOf<PermissionWithKey>()
            val permissions = PermissionEntity.find { PermissionsTable.id inList permissionIds.toList() }
            for (permission in permissions) {
                val category = permission.category
                val module = category.module
                val key = PermissionKey(
                    module = module.name,
                    category = category.name,
                    action = permission.name
                )
                result.add(
                    PermissionWithKey(
                        permission = permission.toDTO(),
                        key = key
                    )
                )
            }
            return@transaction result
        }
    }



    override fun findByName(name: String): Module? {
        return transaction(db) {
            ModuleEntity.find { ModulesTable.name eq name }.firstOrNull()?.toModel()
        }
    }

}