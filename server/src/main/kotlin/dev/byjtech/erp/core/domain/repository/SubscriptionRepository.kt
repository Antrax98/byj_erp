package dev.byjtech.erp.core.domain.repository

import dev.byjtech.erp.core.domain.model.Billing
import dev.byjtech.erp.core.domain.model.Subscription
import java.util.UUID

interface SubscriptionRepository {
    fun create(subscription: Subscription): Subscription
    fun update(subscription: Subscription): Subscription
    fun updateAccessStatus(accessStatus: Boolean, subscriptionId: UUID): Subscription
//    fun updateBilling(billing:Billing): Billing
//    fun findBilling(id: UUID): Billing?
//    fun createBilling(billing: Billing): Billing
    fun find(id: UUID): Subscription?
    fun findByCompanyId(companyId: UUID): Set<Subscription>
    fun findByCompanyIdAndModule(companyId: UUID, module: String): Subscription?
    fun findByModuleId(moduleId: UUID): Set<Subscription>
    fun delete(id: UUID)
//    fun deleteBilling(id: UUID)
}