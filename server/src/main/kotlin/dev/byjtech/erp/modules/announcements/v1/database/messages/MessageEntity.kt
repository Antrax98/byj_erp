package dev.byjtech.erp.modules.announcements.v1.database.messages

import dev.byjtech.erp.core.database.companies.CompanyEntity
import dev.byjtech.erp.core.database.users.UserEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class MessageEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<MessageEntity>(Messages)

    var title by Messages.title
    var content by Messages.content
    var company by CompanyEntity referencedOn Messages.company
    var createdBy by UserEntity optionalReferencedOn Messages.createdBy
    var createdAt by Messages.createdAt
    var expiresAt by Messages.expiresAt
    var isPinned by Messages.isPinned
}