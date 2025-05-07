package dev.byjtech.erp.modules.announcements.v1.database.messages

import dev.byjtech.erp.core.database.companies.Companies
import dev.byjtech.erp.modules.announcements.v1.common.TABLE_PREFIX
import dev.byjtech.erp.core.database.users.Users
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.javatime.datetime


object Messages : IntIdTable("${TABLE_PREFIX}_messages") {
    val title = text("title")
    val content = text("content")
    val company = reference("company_id", Companies)
    val createdBy = reference("created_by", Users).nullable()
    val createdAt = datetime("created_at").nullable()
    val expiresAt = datetime("expires_at").nullable()
    val isPinned = bool("is_pinned").default(false)
}