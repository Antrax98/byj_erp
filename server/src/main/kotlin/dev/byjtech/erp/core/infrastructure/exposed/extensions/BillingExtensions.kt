package dev.byjtech.erp.core.infrastructure.exposed.extensions

import dev.byjtech.erp.core.domain.model.Billing
import dev.byjtech.erp.core.dto.BillingDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.BillingEntity
import dev.byjtech.erp.utils.datetime.toKotlinx

fun BillingEntity.toModel(): Billing {
    return Billing(
        id = this.id.value,
        subscription = this.subscription.toModel(),
        lastPaymentDate = this.lastPaymentDate?.toKotlinx(),
        nextPaymentDue = this.nextPaymentDue?.toKotlinx()
    )
}

fun BillingEntity.toDTO(): BillingDTO {
    return BillingDTO(
        id = this.id.value.toString(),
        subscriptionId = this.subscription.id.value.toString(),
        lastPaymentDate = this.lastPaymentDate?.toKotlinx(),
        nextPaymentDue = this.nextPaymentDue?.toKotlinx()
    )
}