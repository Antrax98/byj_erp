package dev.byjtech.erp.core.infrastructure.exposed.entities

import dev.byjtech.erp.core.infrastructure.exposed.tables.SubscriptionsTable
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import java.util.UUID

class SubscriptionEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<SubscriptionEntity>(
        SubscriptionsTable
    )

    var company by CompanyEntity referencedOn SubscriptionsTable.companyId
    var module by ModuleEntity referencedOn SubscriptionsTable.moduleId
    var isActive by SubscriptionsTable.isActive
    var isAccessible by SubscriptionsTable.isAccessible
    var createdAt by SubscriptionsTable.createdAt
    var updatedAt by SubscriptionsTable.updatedAt
}