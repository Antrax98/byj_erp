package dev.byjtech.erp.core.infrastructure.exposed.repository

import dev.byjtech.erp.core.domain.model.Billing
import dev.byjtech.erp.core.domain.model.Subscription
import dev.byjtech.erp.core.domain.repository.SubscriptionRepository

class SubscriptionRepositoryImpl: SubscriptionRepository {
    override fun create(subscription: Subscription): Subscription {
        TODO("Not yet implemented")
    }

    override fun update(subscription: Subscription): Subscription {
        TODO("Not yet implemented")
    }

    override fun updateBilling(billing: Billing): Billing {
        TODO("Not yet implemented")
    }

    override fun findBilling(id: Int): Billing? {
        TODO("Not yet implemented")
    }

    override fun createBilling(billing: Billing): Billing {
        TODO("Not yet implemented")
    }

    override fun find(id: Int): Subscription? {
        TODO("Not yet implemented")
    }

    override fun findByCompanyId(companyId: Int): Set<Subscription> {
        TODO("Not yet implemented")
    }

    override fun findByModuleId(moduleId: Int): Set<Subscription> {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }
}