package dev.byjtech.erp.core.domain.repository

import dev.byjtech.erp.core.domain.model.Billing
import dev.byjtech.erp.core.domain.model.Subscription

interface SubscriptionRepository {
    fun create(subscription: Subscription): Subscription
    fun update(subscription: Subscription): Subscription
    fun updateBilling(billing:Billing): Billing
    fun findBilling(id: Int): Billing?
    fun createBilling(billing: Billing): Billing
    fun find(id: Int): Subscription?
    fun findByCompanyId(companyId: Int): Set<Subscription>
    fun findByCompanyIdAndModule(companyId: Int, module: String): Subscription?
    fun findByModuleId(moduleId: Int): Set<Subscription>
    fun delete(id: Int)
    fun deleteBilling(id: Int)
}