package dev.byjtech.erp.core.database.companies

import dev.byjtech.erp.core.database.users.Users
import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime


object Companies : IntIdTable("companies") {
    val name = text("name")
    val contactEmail = text("contact_email")
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at")
}