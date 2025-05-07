package dev.byjtech.erp.modules.announcements.v1.dto

import kotlinx.serialization.Serializable
import kotlinx.datetime.LocalDateTime

@Serializable
data class ReadDTO(
    val id: Int,
    val messageId: Int,
    val userId: Int,
    val readAt: LocalDateTime
)
