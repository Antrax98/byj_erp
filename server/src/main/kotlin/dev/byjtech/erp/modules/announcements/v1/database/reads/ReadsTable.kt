package dev.byjtech.erp.modules.announcements.v1.database.reads

import dev.byjtech.erp.core.database.users.Users
import dev.byjtech.erp.modules.announcements.v1.common.TABLE_PREFIX
import dev.byjtech.erp.modules.announcements.v1.database.messages.Messages
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object Reads : IntIdTable("${TABLE_PREFIX}_reads") {
    val message = reference("announcement_id", Messages)
    val user = reference("user_id", Users)
    val readAt = datetime("read_at")
    init {
        index(
            isUnique = true,
            message,
            user
        )
    }
}