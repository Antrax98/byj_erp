package dev.byjtech.erp.modules.document_management.request

import dev.byjtech.erp.modules.document_management.domain.model.DocumentStatus
import kotlinx.datetime.LocalDate
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DocumentSearchRequest(
    @SerialName("document_type")
    val documentType: String? = null,
    
    @SerialName("document_number")
    val documentNumber: String? = null,
    
    @SerialName("status")
    val status: DocumentStatus? = null,
    
    @SerialName("currency")
    val currency: String? = null,
    
    @SerialName("issue_date_from")
    val issueDateFrom: LocalDate? = null,
    
    @SerialName("issue_date_to")
    val issueDateTo: LocalDate? = null,
    
    @SerialName("due_date_from")
    val dueDateFrom: LocalDate? = null,
    
    @SerialName("due_date_to")
    val dueDateTo: LocalDate? = null,
    
    @SerialName("min_amount")
    val minAmount: Double? = null,
    
    @SerialName("max_amount")
    val maxAmount: Double? = null,
    
    @SerialName("created_by")
    val createdBy: String? = null,
    
    @SerialName("search_text")
    val searchText: String? = null, // Búsqueda general en múltiples campos
    
    @SerialName("is_overdue")
    val isOverdue: Boolean? = null, // Filtrar documentos vencidos
    
    @SerialName("include_inactive")
    val includeInactive: Boolean? = null, // Incluir documentos desactivados
    
    @SerialName("page")
    val page: Int = 1,
    
    @SerialName("page_size")
    val pageSize: Int = 20,
    
    @SerialName("sort_by")
    val sortBy: String? = "created_at", // Campo por el que ordenar
    
    @SerialName("sort_direction")
    val sortDirection: SortDirection = SortDirection.DESC
)

@Serializable
enum class SortDirection {
    @SerialName("asc")
    ASC,
    @SerialName("desc") 
    DESC
}
