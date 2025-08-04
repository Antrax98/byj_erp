package dev.byjtech.erp.machinery.domain.repository

import dev.byjtech.erp.machinery.domain.model.MachineryDocument
import kotlinx.datetime.LocalDateTime
import java.util.UUID

interface MachineryDocumentRepository {
    fun save(document: MachineryDocument): MachineryDocument
    fun findById(id: UUID): MachineryDocument?
    fun findByMachineryId(machineryId: UUID): List<MachineryDocument>
    fun findActiveByMachineryId(machineryId: UUID): List<MachineryDocument>
    fun findByDocumentType(documentType: String): List<MachineryDocument>
    fun findByUploadedBy(uploadedBy: UUID): List<MachineryDocument>
    fun findAll(): List<MachineryDocument>
    fun update(document: MachineryDocument): MachineryDocument
    fun delete(id: UUID)
    fun findExpiringDocuments(beforeDate: LocalDateTime): List<MachineryDocument>
    fun findExpiredDocuments(currentDate: LocalDateTime): List<MachineryDocument>
    fun findByFileName(fileName: String): MachineryDocument?
}
