package dev.byjtech.erp.core.domain.repository

import dev.byjtech.erp.core.domain.model.SuperAdmin
import java.util.UUID

interface SuperAdminRepository {
    fun create(superAdmin: SuperAdmin): SuperAdmin
    fun getByUserId(userId: UUID): SuperAdmin?
    fun delete(superAdminId: UUID)
    fun update(superAdmin: SuperAdmin): SuperAdmin
}