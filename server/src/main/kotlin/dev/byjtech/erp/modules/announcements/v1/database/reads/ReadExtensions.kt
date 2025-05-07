package dev.byjtech.erp.modules.announcements.v1.database.reads

import dev.byjtech.erp.modules.announcements.v1.dto.ReadDTO
import kotlinx.datetime.toKotlinLocalDateTime

fun ReadEntity.toDTO() = ReadDTO(
    id = id.value,
    messageId = message.id.value,
    userId = user.id.value,
    readAt = readAt.toKotlinLocalDateTime()
)