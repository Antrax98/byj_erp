package dev.byjtech.erp.core.infrastructure.exposed.extensions

import dev.byjtech.erp.core.domain.model.Billing
import dev.byjtech.erp.core.domain.model.Subscription
import dev.byjtech.erp.core.dto.SubscriptionDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.SubscriptionEntity
import dev.byjtech.erp.utils.datetime.toKotlinx

fun SubscriptionEntity.toDTO(): SubscriptionDTO {
    return SubscriptionDTO(
        id = this.id.value,
        companyId = this.company.id.value,
        moduleId = this.module.id.value,
        isActive = this.isActive,
        isAccessible = this.isAccessible
    )
}

fun SubscriptionEntity.toModel(): Subscription {
    val billing: Billing? = this.billing?.toModel()
    return Subscription(
        id = this.id.value,
        company = this.company.toModel(),
        module = this.module.toModel(),
        isActive = this.isActive,
        isAccessible = this.isAccessible,
        createdAt = this.createdAt?.toKotlinx(),
        updatedAt = this.updatedAt?.toKotlinx(),
        billing = billing
    )
}