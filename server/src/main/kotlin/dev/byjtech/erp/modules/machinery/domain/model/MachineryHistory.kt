package dev.byjtech.erp.modules.machinery.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

data class MachineryHistory(
    val id: UUID = UUID.randomUUID(),
    val machineryId: UUID,
    val userId: UUID,
    val field: String,
    val oldValue: String? = null,
    val newValue: String? = null,
    val createdAt: LocalDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val comment: String? = null
) {
    fun addComment(newComment: String) = copy(comment = newComment)
}
