package dev.byjtech.erp.core.domain.model

import kotlinx.datetime.LocalDateTime
import java.util.UUID

data class Billing (
    val id: UUID,
    val subscription: Subscription,
    val lastPaymentDate: LocalDateTime?,
    val nextPaymentDue: LocalDateTime?
)