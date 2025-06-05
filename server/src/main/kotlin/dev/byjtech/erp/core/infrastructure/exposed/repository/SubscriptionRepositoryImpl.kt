package dev.byjtech.erp.core.infrastructure.exposed.repository

import dev.byjtech.erp.core.domain.model.Billing
import dev.byjtech.erp.core.domain.model.Subscription
import dev.byjtech.erp.core.domain.repository.SubscriptionRepository
import dev.byjtech.erp.core.infrastructure.exposed.entities.BillingEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.CompanyEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.ModuleEntity
import dev.byjtech.erp.core.infrastructure.exposed.entities.SubscriptionEntity
import dev.byjtech.erp.core.infrastructure.exposed.extensions.toModel
import dev.byjtech.erp.core.infrastructure.exposed.tables.SubscriptionsTable
import dev.byjtech.erp.utils.datetime.toJava
import dev.byjtech.erp.utils.datetime.toKotlinx
import org.jetbrains.exposed.sql.transactions.transaction

class SubscriptionRepositoryImpl: SubscriptionRepository {
    override fun create(subscription: Subscription): Subscription {
        return transaction {
            val subscriptionEntity = SubscriptionEntity.new {
                this.company = CompanyEntity[subscription.company.id]
                this.module = ModuleEntity[subscription.module.id]
                this.isActive = subscription.isActive
            }
            return@transaction subscriptionEntity.toModel()
        }
    }

    override fun update(subscription: Subscription): Subscription {
        return transaction {
            val subscriptionEntity = SubscriptionEntity[subscription.id]
            subscriptionEntity.isActive = subscription.isActive
            subscriptionEntity.isAccessible = subscription.isAccessible
            subscriptionEntity.updatedAt = subscription.updatedAt?.toJava()
            subscriptionEntity.billing = subscription.billing?.let { billing ->
                val billingAux = BillingEntity[billing.id]
                billingAux.lastPaymentDate = billing.lastPaymentDate?.toJava()
                billingAux.nextPaymentDue = billing.nextPaymentDue?.toJava()
                return@let billingAux
            }
            return@transaction subscriptionEntity.toModel()
        }
    }

    override fun updateBilling(billing: Billing): Billing {
        return transaction {
            val billingEntity = BillingEntity[billing.id]
            billingEntity.lastPaymentDate = billing.lastPaymentDate?.toJava()
            billingEntity.nextPaymentDue = billing.nextPaymentDue?.toJava()
            return@transaction billingEntity.toModel()
        }
    }

    override fun findBilling(id: Int): Billing? {
        return transaction {
            val billing = BillingEntity.findById(id)
            return@transaction billing?.toModel()
        }
    }

    override fun createBilling(billing: Billing): Billing {
        return transaction {
            val billingEntity = BillingEntity.new {
                this.subscription = SubscriptionEntity[billing.subscription.id]
                this.lastPaymentDate = billing.lastPaymentDate?.toJava()
                this.nextPaymentDue = billing.nextPaymentDue?.toJava()
            }
            return@transaction billingEntity.toModel()
        }
    }

    override fun find(id: Int): Subscription? {
        return transaction {
            val subscription = SubscriptionEntity.findById(id)
            return@transaction subscription?.toModel()
        }
    }

    override fun findByCompanyId(companyId: Int): Set<Subscription> {
        return transaction {
            SubscriptionEntity
                .find { SubscriptionsTable.companyId eq companyId }
                .map { entity ->
                    val company = entity.company.toModel()
                    val module = entity.module.toModel()
                    val billing = entity.billing?.toModel()

                    Subscription(
                        id = entity.id.value,
                        company = company,
                        module = module,
                        isActive = entity.isActive,
                        isAccessible = entity.isAccessible,
                        createdAt = entity.createdAt?.toKotlinx(),
                        updatedAt = entity.updatedAt?.toKotlinx(),
                        billing = billing
                    )
                }
                .toSet()
        }
    }

    override fun findByCompanyIdAndModule(companyId: Int, module: String): Subscription? {
        return transaction {
            val subscriptions = SubscriptionEntity.find { SubscriptionsTable.companyId eq companyId }
            val subscription = subscriptions.find { it.module.name == module }
            subscription?.toModel()
        }
    }

    override fun findByModuleId(moduleId: Int): Set<Subscription> {
        return transaction {
            val subscriptions = SubscriptionEntity.find {SubscriptionsTable.moduleId eq moduleId}
            return@transaction subscriptions.map { it.toModel() }.toSet()
        }
    }

    override fun delete(id: Int) {
        transaction {
            SubscriptionEntity[id].delete()
        }
    }

    override fun deleteBilling(id: Int) {
        transaction {
            BillingEntity[id].delete()
        }
    }
}