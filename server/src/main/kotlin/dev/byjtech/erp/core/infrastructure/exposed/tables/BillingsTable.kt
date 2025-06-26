package dev.byjtech.erp.core.infrastructure.exposed.tables

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime

object BillingsTable: UUIDTable("billings") {
    val company = reference("company", CompaniesTable, onDelete = ReferenceOption.CASCADE)
    val lastPaymentDate = datetime("last_payment_date").nullable()
    val nextPaymentDue = datetime("next_payment_due").nullable()
}