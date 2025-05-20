package dev.byjtech.erp.core.domain.repository

import dev.byjtech.erp.core.domain.model.SuperAdmin

interface SuperAdminRepository {
    fun create(superAdmin: SuperAdmin): SuperAdmin
    fun getByUserId(userId: Int): SuperAdmin?
    fun delete(superAdminId: Int)
    fun update(superAdmin: SuperAdmin): SuperAdmin
}