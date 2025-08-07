package dev.byjtech.erp.modules.machinery.infrastructure.exposed.repository

import dev.byjtech.erp.modules.machinery.domain.model.MachineryDocument
import dev.byjtech.erp.modules.machinery.domain.repository.MachineryDocumentRepository
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities.MachineryDocumentEntity
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.entities.MachineryEntity
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.extensions.fromModel
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.extensions.toModel
import dev.byjtech.erp.modules.machinery.infrastructure.exposed.tables.MachineryDocumentsTable
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction
import java.util.UUID

class MachineryDocumentRepositoryImpl(private val db: Database) : MachineryDocumentRepository {
    
    override fun save(document: MachineryDocument): MachineryDocument {
        return transaction(db) {
            val machinery = MachineryEntity.findById(document.machineryId)
                ?: throw IllegalArgumentException("Machinery with id ${document.machineryId} not found")
            
            val entity = MachineryDocumentEntity.new {
                this.machinery = machinery
                documentType = document.documentType
                description = document.description
                fileName = document.fileName
                filePath = document.filePath
                issueDate = document.issueDate?.toJavaLocalDateTime()
                expirationDate = document.expirationDate?.toJavaLocalDateTime()
                uploadedBy = document.uploadedBy
                createdAt = java.time.LocalDateTime.now()
                updatedAt = java.time.LocalDateTime.now()
                isActive = document.isActive
            }
            entity.toModel()
        }
    }

    override fun findById(id: UUID): MachineryDocument? {
        return transaction(db) {
            MachineryDocumentEntity.findById(id)?.toModel()
        }
    }

    override fun findByMachineryId(machineryId: UUID): List<MachineryDocument> {
        return transaction(db) {
            MachineryDocumentEntity.find { MachineryDocumentsTable.machineryId eq machineryId }
                .map { it.toModel() }
        }
    }

    override fun findActiveByMachineryId(machineryId: UUID): List<MachineryDocument> {
        return transaction(db) {
            MachineryDocumentEntity.find { 
                (MachineryDocumentsTable.machineryId eq machineryId) and 
                (MachineryDocumentsTable.isActive eq true)
            }.map { it.toModel() }
        }
    }

    override fun findByDocumentType(documentType: String): List<MachineryDocument> {
        return transaction(db) {
            MachineryDocumentEntity.find { MachineryDocumentsTable.documentType eq documentType }
                .map { it.toModel() }
        }
    }

    override fun findByUploadedBy(uploadedBy: UUID): List<MachineryDocument> {
        return transaction(db) {
            MachineryDocumentEntity.find { MachineryDocumentsTable.uploadedBy eq uploadedBy }
                .map { it.toModel() }
        }
    }

    override fun findAll(): List<MachineryDocument> {
        return transaction(db) {
            MachineryDocumentEntity.all().map { it.toModel() }
        }
    }

    override fun update(document: MachineryDocument): MachineryDocument {
        return transaction(db) {
            val entity = MachineryDocumentEntity.findById(document.id)
                ?: throw IllegalArgumentException("MachineryDocument with id ${document.id} not found")
            entity.fromModel(document)
            entity.updatedAt = java.time.LocalDateTime.now()
            entity.toModel()
        }
    }

    override fun delete(id: UUID) {
        transaction(db) {
            MachineryDocumentEntity.findById(id)?.delete()
        }
    }

    override fun findExpiringDocuments(beforeDate: LocalDateTime): List<MachineryDocument> {
        return transaction(db) {
            MachineryDocumentEntity.find { 
                (MachineryDocumentsTable.expirationDate lessEq beforeDate.toJavaLocalDateTime()) and
                (MachineryDocumentsTable.isActive eq true)
            }.map { it.toModel() }
        }
    }

    override fun findExpiredDocuments(currentDate: LocalDateTime): List<MachineryDocument> {
        return transaction(db) {
            MachineryDocumentEntity.find { 
                (MachineryDocumentsTable.expirationDate less currentDate.toJavaLocalDateTime()) and
                (MachineryDocumentsTable.isActive eq true)
            }.map { it.toModel() }
        }
    }

    override fun findByFileName(fileName: String): MachineryDocument? {
        return transaction(db) {
            MachineryDocumentEntity.find { MachineryDocumentsTable.fileName eq fileName }
                .firstOrNull()?.toModel()
        }
    }
}
