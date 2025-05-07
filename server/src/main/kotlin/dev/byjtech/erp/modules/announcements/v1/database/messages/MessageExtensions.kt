package dev.byjtech.erp.modules.announcements.v1.database.messages

import dev.byjtech.erp.modules.announcements.v1.dto.MessageDTO
import kotlinx.datetime.toKotlinLocalDateTime

fun MessageEntity.toDTO() = MessageDTO(
    id = id.value,
    title = title,
    content = content,
    companyId = company.id.value,
    createdBy = createdBy?.id?.value,
    createdAt = createdAt?.toKotlinLocalDateTime(),
    expiresAt = expiresAt?.toKotlinLocalDateTime(),
    isPinned = isPinned
)