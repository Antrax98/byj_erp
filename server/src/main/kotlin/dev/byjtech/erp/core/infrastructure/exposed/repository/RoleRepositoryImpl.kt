package dev.byjtech.erp.core.infrastructure.exposed.repository

import dev.byjtech.erp.core.domain.model.Role
import dev.byjtech.erp.core.domain.repository.RoleRepository

class RoleRepositoryImpl: RoleRepository {
    override fun create(role: Role): Role {
        TODO("Not yet implemented")
    }
    override fun getById(roleId: Int): Role? {
        TODO("Not yet implemented")
    }
    override fun delete(roleId: Int) {
        TODO("Not yet implemented")
    }
    override fun addPermission(roleId: Int, permissionId: Int): Role {
        TODO("Not yet implemented")
    }
    override fun removePermission(roleId: Int, permissionId: Int): Role {
        TODO("Not yet implemented")

    }
}