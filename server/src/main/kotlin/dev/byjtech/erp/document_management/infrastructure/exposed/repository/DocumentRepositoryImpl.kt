package dev.byjtech.erp.document_management.infrastructure.exposed.repository

import dev.byjtech.erp.document_management.domain.model.Document
import dev.byjtech.erp.document_management.domain.repository.DocumentRepository
import dev.byjtech.erp.document_management.domain.repository.SearchResult
import dev.byjtech.erp.document_management.infrastructure.exposed.tables.DocumentsTable
import dev.byjtech.erp.modules.document_management.request.DocumentSearchRequest
import dev.byjtech.erp.modules.document_management.request.SortDirection
import dev.byjtech.erp.modules.document_management.domain.model.DocumentStatus
import dev.byjtech.erp.modules.document_management.domain.model.DocumentType
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.like
import org.jetbrains.exposed.sql.SqlExpressionBuilder.greaterEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.lessEq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.less
import org.jetbrains.exposed.sql.SqlExpressionBuilder.neq
import org.jetbrains.exposed.sql.SqlExpressionBuilder.isNotNull
import org.jetbrains.exposed.sql.SqlExpressionBuilder.inList
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.Database
import java.util.UUID

class DocumentRepositoryImpl(private val database: Database) : DocumentRepository {

    override fun findById(id: UUID): Document? = transaction(database) {
        DocumentsTable.selectAll().where { DocumentsTable.id eq id }
            .map { it.toDomain() }
            .singleOrNull()
    }

    override fun findAll(): List<Document> = transaction(database) {
        DocumentsTable.selectAll()
            .map { it.toDomain() }
    }

    override fun findByCompanyId(companyId: UUID): List<Document> = transaction(database) {
        DocumentsTable.selectAll().where { DocumentsTable.companyId eq companyId }
            .map { it.toDomain() }
    }

    override fun search(companyId: UUID, searchRequest: DocumentSearchRequest): SearchResult = transaction(database) {
        val baseQuery = DocumentsTable.selectAll().where { 
            DocumentsTable.companyId eq companyId and (DocumentsTable.active eq true)
        }
        
        // Aplicar filtros
        var query = baseQuery
        val conditions = mutableListOf<Op<Boolean>>()
        
        // Filtros específicos
        searchRequest.documentType?.let { type ->
            // Para enums, buscar por coincidencia exacta o convertir el enum name a string para búsqueda parcial
            try {
                val documentType = DocumentType.valueOf(type.uppercase())
                conditions.add(DocumentsTable.type eq documentType)
            } catch (e: IllegalArgumentException) {
                // Si no es un valor de enum válido, buscar en el nombre del enum como string
                val matchingTypes = DocumentType.entries.filter { 
                    it.name.contains(type, ignoreCase = true) 
                }
                if (matchingTypes.isNotEmpty()) {
                    conditions.add(DocumentsTable.type inList matchingTypes)
                }
            }
        }
        
        searchRequest.documentNumber?.let { number ->
            conditions.add(DocumentsTable.documentNumber like "%$number%")
        }
        
        searchRequest.status?.let { status ->
            conditions.add(DocumentsTable.status eq status)
        }
        
        searchRequest.currency?.let { currency ->
            conditions.add(DocumentsTable.currency like "%$currency%")
        }
        
        searchRequest.issueDateFrom?.let { dateFrom ->
            conditions.add(DocumentsTable.issueDate greaterEq dateFrom.toJavaLocalDate())
        }
        
        searchRequest.issueDateTo?.let { dateTo ->
            conditions.add(DocumentsTable.issueDate lessEq dateTo.toJavaLocalDate())
        }
        
        searchRequest.dueDateFrom?.let { dateFrom ->
            conditions.add(DocumentsTable.dueDate greaterEq dateFrom.toJavaLocalDate())
        }
        
        searchRequest.dueDateTo?.let { dateTo ->
            conditions.add(DocumentsTable.dueDate lessEq dateTo.toJavaLocalDate())
        }
        
        searchRequest.minAmount?.let { minAmount ->
            conditions.add(DocumentsTable.totalAmount greaterEq minAmount.toBigDecimal())
        }
        
        searchRequest.maxAmount?.let { maxAmount ->
            conditions.add(DocumentsTable.totalAmount lessEq maxAmount.toBigDecimal())
        }
        
        searchRequest.createdBy?.let { createdBy ->
            conditions.add(DocumentsTable.createdBy eq UUID.fromString(createdBy))
        }
        
        // Búsqueda de texto general
        searchRequest.searchText?.let { searchText ->
            // Para la búsqueda de texto, buscar en tipos de documento que contengan el texto en sus nombres
            val matchingTypes = DocumentType.entries.filter { 
                it.name.contains(searchText, ignoreCase = true) 
            }
            
            val textSearchCondition = if (matchingTypes.isNotEmpty()) {
                DocumentsTable.type.inList(matchingTypes) or
                        DocumentsTable.documentNumber.like("%$searchText%") or
                        DocumentsTable.currency.like("%$searchText%")
            } else {
                DocumentsTable.documentNumber.like("%$searchText%") or
                        DocumentsTable.currency.like("%$searchText%")
            }
            conditions.add(textSearchCondition)
        }
        
        // Filtro de documentos vencidos
        searchRequest.isOverdue?.let { isOverdue ->
            if (isOverdue) {
                val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
                val overdueCondition = DocumentsTable.dueDate.isNotNull() and
                        (DocumentsTable.dueDate less today.toJavaLocalDate()) and
                        (DocumentsTable.status neq DocumentStatus.APPROVED)
                conditions.add(overdueCondition)
            }
        }
        
        // Aplicar todas las condiciones
        if (conditions.isNotEmpty()) {
            query = query.andWhere { conditions.reduce { acc, condition -> acc and condition } }
        }
        
        // Contar total de resultados
        val totalCount = query.count()
        
        // Aplicar ordenamiento
        val sortColumn = when (searchRequest.sortBy) {
            "document_type" -> DocumentsTable.type
            "document_number" -> DocumentsTable.documentNumber
            "issue_date" -> DocumentsTable.issueDate
            "due_date" -> DocumentsTable.dueDate
            "status" -> DocumentsTable.status
            "total_amount" -> DocumentsTable.totalAmount
            "updated_at" -> DocumentsTable.updatedAt
            else -> DocumentsTable.createdAt
        }
        
        query = when (searchRequest.sortDirection) {
            SortDirection.ASC -> query.orderBy(sortColumn to SortOrder.ASC)
            SortDirection.DESC -> query.orderBy(sortColumn to SortOrder.DESC)
        }
        
        // Aplicar paginación
        val offset = (searchRequest.page - 1) * searchRequest.pageSize
        val documents = query
            .drop(offset)
            .take(searchRequest.pageSize)
            .map { it.toDomain() }
        
        return@transaction SearchResult(documents, totalCount)
    }

    override fun save(document: Document): Document = transaction(database) {
        val insertedId = DocumentsTable.insertAndGetId { row ->
            row[type] = document.type
            row[documentNumber] = document.documentNumber
            row[companyId] = document.companyId!!
            row[issueDate] = document.issueDate.toJavaLocalDate()
            row[dueDate] = document.dueDate?.toJavaLocalDate()
            row[status] = document.status
            row[currency] = document.currency
            row[netAmount] = document.netAmount.toBigDecimal()
            row[taxAmount] = document.taxAmount.toBigDecimal()
            row[totalAmount] = document.totalAmount.toBigDecimal()
            row[fileUrl] = document.fileUrl
            row[createdBy] = document.createdBy
            row[createdAt] = document.createdAt.toJavaLocalDateTime()
            row[updatedAt] = document.updatedAt.toJavaLocalDateTime()
            row[active] = document.active
        }.value

        return@transaction findById(insertedId) ?: throw IllegalStateException("Failed to retrieve saved document with ID: $insertedId")
    }

    override fun update(document: Document): Document = transaction(database) {
        DocumentsTable.update({ DocumentsTable.id eq document.id }) { row ->
            row[type] = document.type
            row[documentNumber] = document.documentNumber
            row[companyId] = document.companyId!!
            row[issueDate] = document.issueDate.toJavaLocalDate()
            row[dueDate] = document.dueDate?.toJavaLocalDate()
            row[status] = document.status
            row[currency] = document.currency
            row[netAmount] = document.netAmount.toBigDecimal()
            row[taxAmount] = document.taxAmount.toBigDecimal()
            row[totalAmount] = document.totalAmount.toBigDecimal()
            row[fileUrl] = document.fileUrl
            row[createdBy] = document.createdBy
            row[createdAt] = document.createdAt.toJavaLocalDateTime()
            row[updatedAt] = document.updatedAt.toJavaLocalDateTime()
            row[active] = document.active
        }
        findById(document.id)!!
    }

    override fun delete(id: UUID): Unit = transaction(database) {
        DocumentsTable.deleteWhere { DocumentsTable.id eq id }
    }

    private fun ResultRow.toDomain(): Document = Document(
        id = this[DocumentsTable.id].value,
        type = this[DocumentsTable.type],
        documentNumber = this[DocumentsTable.documentNumber],
        issueDate = this[DocumentsTable.issueDate].toKotlinLocalDate(),
        dueDate = this[DocumentsTable.dueDate]?.toKotlinLocalDate(),
        status = this[DocumentsTable.status],
        currency = this[DocumentsTable.currency],
        netAmount = this[DocumentsTable.netAmount].toDouble(),
        taxAmount = this[DocumentsTable.taxAmount].toDouble(),
        totalAmount = this[DocumentsTable.totalAmount].toDouble(),
        fileUrl = this[DocumentsTable.fileUrl],
        createdBy = this[DocumentsTable.createdBy],
        createdAt = this[DocumentsTable.createdAt].toKotlinLocalDateTime(),
        updatedAt = this[DocumentsTable.updatedAt].toKotlinLocalDateTime(),
        active = this[DocumentsTable.active],
        companyId = this[DocumentsTable.companyId]
    )
}