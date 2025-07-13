package dev.byjtech.erp.core.domain.repository

import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.PermissionWithKey
import dev.byjtech.erp.core.domain.model.Category
import dev.byjtech.erp.core.domain.model.Permission
import dev.byjtech.erp.core.dto.CategoryDTO //TODO() crear un DTO especifico para crear categorys???
import dev.byjtech.erp.core.dto.PermissionDTO
import java.util.UUID
import dev.byjtech.erp.core.domain.model.Module

interface ModuleRepository {
    fun create(module: Module): Module
    fun get(moduleId: UUID): Module? //solo el module
    fun getWithCategories(moduleId: UUID): Module? //con categories
    fun getWithCategoriesAndPermissions(moduleId: UUID): Module? //con categories y permissions
    fun getCategoryById(categoryId: UUID): Category?
    fun getPermissionById(permissionId: UUID): Permission?
    fun delete(moduleId: UUID)
    fun getCategoryByPermissionId(permissionId: UUID): Category?
    fun getByCategoryId(categoryId: UUID): Module?
    fun getByPermissionId(permissionId: UUID): Module?
    fun addCategory(moduleId: UUID, newCategory: CategoryDTO): Category
    fun addPermissionToCategory(categoryId: UUID, newPermission: PermissionDTO): Permission
    fun findPermissionByPermissionKey(permissionKey: PermissionKey): Permission?
    fun findPermissionsByPermissionKeySet(permissionKeySet: Set<PermissionKey>): Set<Permission>
    fun getPermissionKeysByPermissionIdSet(permissionIdSet: Set<UUID>): Set<PermissionKey>
    fun getPermissionKeyById(permissionId: UUID): PermissionKey?
    fun getPermissionKeysByModuleId(moduleId: UUID): Set<PermissionKey>
    fun getPermissionsByModuleId(moduleId: UUID): Set<Permission>
    fun getPermissionsWithKeysByModuleIds(moduleIds: Set<UUID>): Set<PermissionWithKey>
    fun getPermissionsWithKeysByPermissionIds(permissionIds: Set<UUID>): Set<PermissionWithKey>
    fun findByName(name: String): Module?
    //TODo() si faltan mas se agregan nomas
}