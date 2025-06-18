package dev.byjtech.erp.core.infrastructure.exposed.repository

import dev.byjtech.erp.common.PermissionKey
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

class ModuleRepositoryImpl(private val db: Database): ModuleRepository {
    override fun create(module: Module): Module {
        TODO("Not yet implemented")
    }

    override fun get(moduleId: UUID): Module? {
        TODO("Not yet implemented")
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

}