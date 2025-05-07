package dev.byjtech.erp.modules.announcements.v1.database.reads

import dev.byjtech.erp.core.database.users.UserEntity
import dev.byjtech.erp.modules.announcements.v1.database.messages.MessageEntity
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ReadEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<ReadEntity>(Reads)

    var message by MessageEntity referencedOn Reads.message
    var user by UserEntity referencedOn Reads.user
    var readAt by Reads.readAt
}