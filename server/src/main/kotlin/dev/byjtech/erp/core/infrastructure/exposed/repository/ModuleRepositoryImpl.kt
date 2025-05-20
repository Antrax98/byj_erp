package dev.byjtech.erp.core.infrastructure.exposed.repository

import dev.byjtech.erp.core.domain.model.Category
import dev.byjtech.erp.core.domain.model.Permission
import dev.byjtech.erp.core.domain.repository.ModuleRepository
import dev.byjtech.erp.core.dto.CategoryDTO
import dev.byjtech.erp.core.dto.PermissionDTO

class ModuleRepositoryImpl: ModuleRepository {
    override fun create(module: Module): Module {
        TODO("Not yet implemented")
    }

    override fun get(moduleId: Int): Module? {
        TODO("Not yet implemented")
    }

    override fun getWithCategories(moduleId: Int): Module? {
        TODO("Not yet implemented")
    }

    override fun getWithCategoriesAndPermissions(moduleId: Int): Module? {
        TODO("Not yet implemented")
    }

    override fun getCategoryById(categoryId: Int): Category? {
        TODO("Not yet implemented")
    }

    override fun getPermissionById(permissionId: Int): Permission? {
        TODO("Not yet implemented")
    }

    override fun delete(moduleId: Int) {
        TODO("Not yet implemented")
    }

    override fun getCategoryByPermissionId(permissionId: Int): Category? {
        TODO("Not yet implemented")
    }

    override fun getByCategoryId(categoryId: Int): Module? {
        TODO("Not yet implemented")
    }

    override fun getByPermissionId(permissionId: Int): Module? {
        TODO("Not yet implemented")
    }

    override fun addCategory(moduleId: Int, newCategory: CategoryDTO): Category {
        TODO("Not yet implemented")
    }

    override fun addPermissionToCategory(
        categoryId: Int,
        newPermission: PermissionDTO
    ): Permission {
        TODO("Not yet implemented")
    }
}