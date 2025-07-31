package dev.byjtech.erp.core.response

import dev.byjtech.erp.core.dto.ModuleDTO
import dev.byjtech.erp.core.dto.SubscriptionDTO
import kotlinx.serialization.Serializable

@Serializable
data class SubscriptionsModResponse (
    val subscription: SubscriptionDTO,
    val module: ModuleDTO
)