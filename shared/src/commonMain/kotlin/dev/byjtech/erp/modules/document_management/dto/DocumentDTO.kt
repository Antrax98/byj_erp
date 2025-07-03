package shared.src.commonMain.kotlin.dev.byjtech.erp.modules.document_management.dto
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

data class DocumentDTO(
    val id: String,
    val documentType: String,
    val documentNumber: String,
    val issueDate: LocalDate,
    val dueDate: LocalDate?,
    val status: String,
    val currency: String,
    val netAmount: Double,
    val taxAmount: Double,
    val totalAmount: Double,
    val fileUrl: String,
    val createdBy: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime,
    val active: Boolean
)
