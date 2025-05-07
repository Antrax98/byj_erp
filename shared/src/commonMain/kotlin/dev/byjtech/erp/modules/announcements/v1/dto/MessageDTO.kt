package dev.byjtech.erp.modules.announcements.v1.dto

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class MessageDTO(
    val id: Int,
    val title: String,
    val content: String,
    val companyId: Int,
    val createdBy: Int?,
    val createdAt: LocalDateTime?,
    val expiresAt: LocalDateTime?,
    val isPinned: Boolean
)