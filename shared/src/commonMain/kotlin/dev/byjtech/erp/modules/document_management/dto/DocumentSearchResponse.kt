package dev.byjtech.erp.modules.document_management.dto

import dev.byjtech.erp.document_management.dto.DocumentDTO
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DocumentSearchResponse(
    @SerialName("documents")
    val documents: List<DocumentDTO>,
    
    @SerialName("total_count")
    val totalCount: Long,
    
    @SerialName("page")
    val page: Int,
    
    @SerialName("page_size")
    val pageSize: Int,
    
    @SerialName("total_pages")
    val totalPages: Int,
    
    @SerialName("has_next_page")
    val hasNextPage: Boolean,
    
    @SerialName("has_previous_page")
    val hasPreviousPage: Boolean
) {
    companion object {
        fun create(
            documents: List<DocumentDTO>,
            totalCount: Long,
            page: Int,
            pageSize: Int
        ): DocumentSearchResponse {
            val totalPages = if (totalCount == 0L) 1 else ((totalCount + pageSize - 1) / pageSize).toInt()
            
            return DocumentSearchResponse(
                documents = documents,
                totalCount = totalCount,
                page = page,
                pageSize = pageSize,
                totalPages = totalPages,
                hasNextPage = page < totalPages,
                hasPreviousPage = page > 1
            )
        }
    }
}
