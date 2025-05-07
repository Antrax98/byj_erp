package dev.byjtech.erp.core.database.superAdmins

import dev.byjtech.erp.core.database.users.Users
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class SuperAdminEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<SuperAdminEntity>(SuperAdmins)

    var userId by SuperAdmins.userId
    var description by SuperAdmins.description // Descripción del superadmin
}