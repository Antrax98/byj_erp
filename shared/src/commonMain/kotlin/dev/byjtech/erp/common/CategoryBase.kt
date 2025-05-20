package dev.byjtech.erp.common

abstract class CategoryBase(
    override val name: String,
    override val description: String,
    override val module: ModuleDefinition
) : Category {
    private val _permissions = mutableSetOf<Permission>()
    override val permissions: Set<Permission> get() = _permissions

    protected fun permission(action: String, description: String): Permission {
        val perm = object : Permission {
            override val action = action
            override val description = description
            override val module = this@CategoryBase.module
            override val category = this@CategoryBase
        }
        _permissions += perm
        return perm
    }
}