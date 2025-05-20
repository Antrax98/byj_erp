package dev.byjtech.erp.core.infrastructure.exposed.extensions

import dev.byjtech.erp.core.infrastructure.exposed.entities.SubscriptionEntity

fun SubscriptionEntity.toDTO(): dev.byjtech.erp.core.dto.SubscriptionDTO {
    return dev.byjtech.erp.core.dto.SubscriptionDTO(
        id = this.id.value,
        companyId = this.company.id.value,
        moduleId = this.module.id.value,
        isActive = this.isActive,
        isAccessible = this.isAccessible
    )
}