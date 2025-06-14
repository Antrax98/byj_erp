package dev.byjtech.erp.core.database.superAdmins

import dev.byjtech.erp.core.infrastructure.exposed.entities.SuperAdminEntity
import dev.byjtech.erp.core.infrastructure.exposed.tables.SuperAdminsTable
import org.jetbrains.exposed.sql.transactions.transaction


//TODO: ELIMINAR y reemplazar por un service de roles y/o permisos
//class SuperAdminDataSource {
//    companion object{
//        fun isSuperAdmin(userId: Int): Boolean {
//            return transaction {
//                SuperAdminEntity.find { SuperAdminsTable.userId eq userId }.firstOrNull() != null
//            }
//        }
//    }
//}