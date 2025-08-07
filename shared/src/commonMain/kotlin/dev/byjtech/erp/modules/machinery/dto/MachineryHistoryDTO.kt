package dev.byjtech.erp.modules.machinery.dto

import kotlinx.serialization.Serializable
import kotlinx.datetime.Instant

@Serializable
data class MachineryHistoryDTO(
    val id: String,
    val machineryId: String,
    val machineryName: String,
    val changeType: String,
    val changeDescription: String,
    val changedBy: String,
    val changeDate: Instant,
    val oldValues: String?,
    val newValues: String?
)
