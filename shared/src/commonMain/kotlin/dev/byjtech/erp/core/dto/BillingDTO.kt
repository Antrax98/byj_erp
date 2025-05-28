package dev.byjtech.erp.core.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class BillingDTO(
    val id: Int,
    val subscriptionId: Int,
    val lastPaymentDate: LocalDateTime?,
    val nextPaymentDue: LocalDateTime?
)