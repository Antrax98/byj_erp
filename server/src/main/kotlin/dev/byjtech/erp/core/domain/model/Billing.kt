package dev.byjtech.erp.core.domain.model

import kotlinx.datetime.LocalDateTime

data class Billing (
    val id: Int,
    val subscription: Subscription,
    val lastPaymentDate: LocalDateTime?,
    val nextPaymentDue: LocalDateTime?
)