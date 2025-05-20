package dev.byjtech.erp.core.infrastructure.exposed.entities

import dev.byjtech.erp.core.infrastructure.exposed.tables.BillingsTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class BillingEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<BillingEntity>(BillingsTable)

    var subscription by SubscriptionEntity referencedOn BillingsTable.subscriptionId
    var lastPaymentDate by BillingsTable.lastPaymentDate
    var nextPaymentDue by BillingsTable.nextPaymentDue
}