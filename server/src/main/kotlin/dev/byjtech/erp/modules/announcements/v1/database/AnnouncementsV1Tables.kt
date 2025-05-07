package dev.byjtech.erp.modules.announcements.v1.database

import org.jetbrains.exposed.sql.Table
import dev.byjtech.erp.modules.announcements.v1.database.messages.Messages
import dev.byjtech.erp.modules.announcements.v1.database.reads.Reads

object AnnouncementsV1Tables {
    val all = listOf<Table>(
        Messages,
        Reads
    )
}