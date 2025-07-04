package dev.byjtech.erp.document_management.infrastructure.exposed.entities

import dev.byjtech.erp.core.infrastructure.exposed.entities.UserEntity
import dev.byjtech.erp.document_management.domain.model.DocumentEditHistory
import dev.byjtech.erp.document_management.infrastructure.exposed.tables.DocumentEditHistoryTable
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DocumentEditHistoryEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DocumentEditHistoryEntity>(DocumentEditHistoryTable)

    var document by DocumentEntity referencedOn DocumentEditHistoryTable.documentId
    var fieldName by DocumentEditHistoryTable.fieldName
    var oldValue by DocumentEditHistoryTable.oldValue
    var newValue by DocumentEditHistoryTable.newValue
    var user by UserEntity referencedOn DocumentEditHistoryTable.userId
    var createdAt by DocumentEditHistoryTable.createdAt

    fun toDomain() = DocumentEditHistory( //permite usar los modelos sin importar cómo se guarden
        id = id.value,
        documentId = document.id.value,
        fieldName = fieldName,
        oldValue = oldValue,
        newValue = newValue,
        userId = user.id.value,
        createdAt = createdAt.toKotlinLocalDateTime()
    )
}
