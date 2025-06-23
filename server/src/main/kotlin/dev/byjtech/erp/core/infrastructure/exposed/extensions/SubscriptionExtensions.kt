package dev.byjtech.erp.core.infrastructure.exposed.extensions

import dev.byjtech.erp.core.domain.model.Billing
import dev.byjtech.erp.core.domain.model.Company
import dev.byjtech.erp.core.domain.model.Subscription
import dev.byjtech.erp.core.domain.model.Module
import dev.byjtech.erp.core.dto.SubscriptionDTO
import dev.byjtech.erp.core.infrastructure.exposed.entities.SubscriptionEntity
import dev.byjtech.erp.utils.datetime.toKotlinx

fun SubscriptionEntity.toDTO(): SubscriptionDTO {
    return SubscriptionDTO(
        id = this.id.value.toString(),
        companyId = this.company.id.value.toString(),
        moduleId = this.module.id.value.toString(),
        isActive = this.isActive,
        isAccessible = this.isAccessible
    )
}

//no usar, solo como ejemplo
//fun SubscriptionEntity.toModel(billing: Billing? = null): Subscription {
//    return Subscription(
//        id = this.id.value,
//        company = this.company.toModel(),
//        module = this.module.toModel(),
//        isActive = this.isActive,
//        isAccessible = this.isAccessible,
//        createdAt = this.createdAt?.toKotlinx(),
//        updatedAt = this.updatedAt?.toKotlinx(),
//        billing = billing
//    )
//}

fun SubscriptionEntity.toModel(
    company: Company = this.company.toModel(),
    module: Module = this.module.toModel(),
): Subscription {
    return Subscription(
        id = this.id.value,
        company = company,
        module = module,
        isActive = this.isActive,
        isAccessible = this.isAccessible,
        createdAt = this.createdAt?.toKotlinx(),
        updatedAt = this.updatedAt?.toKotlinx(),
    )
}