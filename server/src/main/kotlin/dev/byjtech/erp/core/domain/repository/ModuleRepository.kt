package dev.byjtech.erp.core.domain.repository

import dev.byjtech.erp.core.domain.model.Category
import dev.byjtech.erp.core.domain.model.Permission
import dev.byjtech.erp.core.dto.CategoryDTO //TODO() crear un DTO especifico para crear categorys???
import dev.byjtech.erp.core.dto.PermissionDTO

interface ModuleRepository {
    fun create(module: Module): Module
    fun get(moduleId: Int): Module? //solo el module
    fun getWithCategories(moduleId: Int): Module? //con categories
    fun getWithCategoriesAndPermissions(moduleId: Int): Module? //con categories y permissions
    fun getCategoryById(categoryId: Int): Category?
    fun getPermissionById(permissionId: Int): Permission?
    fun delete(moduleId: Int)
    fun getCategoryByPermissionId(permissionId: Int): Category?
    fun getByCategoryId(categoryId: Int): Module?
    fun getByPermissionId(permissionId: Int): Module?
    fun addCategory(moduleId: Int, newCategory: CategoryDTO): Category
    fun addPermissionToCategory(categoryId: Int, newPermission: PermissionDTO): Permission
    //TODo() si faltan mas se agregan nomas
}