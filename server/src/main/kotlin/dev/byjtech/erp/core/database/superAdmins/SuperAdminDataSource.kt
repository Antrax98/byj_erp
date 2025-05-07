package dev.byjtech.erp.core.database.superAdmins

import org.jetbrains.exposed.sql.transactions.transaction

class SuperAdminDataSource {
    companion object{
        fun isSuperAdmin(userId: Int): Boolean {
            return transaction {
                SuperAdminEntity.find { SuperAdmins.userId eq userId }.firstOrNull() != null
            }
        }
    }
}