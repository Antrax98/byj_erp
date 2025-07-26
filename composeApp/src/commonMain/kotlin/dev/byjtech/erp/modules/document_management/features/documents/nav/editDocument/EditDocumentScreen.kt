package dev.byjtech.erp.modules.document_management.features.documents.nav.editDocument

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.document_management.dto.DocumentDTO
import dev.byjtech.erp.document_management.request.UpdateDocumentRequest
import dev.byjtech.erp.modules.document_management.features.documents.DocumentsFeatureComponentImpl
import dev.byjtech.erp.common.ui.DatePickerField
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

data class EditDocumentState(
    val isLoading: Boolean = false,
    val document: DocumentDTO? = null,
    val documentType: String = "",
    val documentNumber: String = "",
    val issueDate: LocalDate? = null,
    val dueDate: LocalDate? = null,
    val currency: String = "CLP",
    val netAmount: String = "",
    val taxAmount: String = "",
    val totalAmount: String = "",
    val fileUrl: String = "",
    val status: String = "",
    val error: String? = null,
    val isValid: Boolean = false,
    val showDeleteDialog: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditDocumentScreen(component: EditDocumentComponent) {
    var state by remember { mutableStateOf(EditDocumentState()) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()

    // Cargar documento al inicializar
    LaunchedEffect(component.documentId) {
        state = state.copy(isLoading = true)
        try {
            val document = component.apiClient.documentManagement.getDocumentById(component.documentId)
            if (document != null) {
                state = state.copy(
                    isLoading = false,
                    document = document,
                    documentType = document.documentType,
                    documentNumber = document.documentNumber,
                    issueDate = document.issueDate,
                    dueDate = document.dueDate,
                    currency = document.currency,
                    netAmount = document.netAmount.toString(),
                    taxAmount = document.taxAmount.toString(),
                    totalAmount = document.totalAmount.toString(),
                    fileUrl = document.fileUrl,
                    status = document.status.name
                )
            } else {
                state = state.copy(
                    isLoading = false,
                    error = "Documento no encontrado"
                )
            }
        } catch (e: Exception) {
            state = state.copy(
                isLoading = false,
                error = "Error al cargar documento: ${e.message}"
            )
        }
    }

    // Validar formulario
    LaunchedEffect(state.documentType, state.documentNumber, state.issueDate, state.dueDate, state.netAmount) {
        val today = LocalDate(2025, 7, 25) // Fecha actual
        val minValidDate = if (state.issueDate != null) {
            if (state.issueDate!! > today) state.issueDate!! else today
        } else {
            today
        }
        
        val isDueDateValid = state.dueDate == null || state.dueDate!! >= minValidDate
        
        state = state.copy(
            isValid = state.documentType.isNotBlank() &&
                    state.documentNumber.isNotBlank() &&
                    state.issueDate != null &&
                    state.netAmount.isNotBlank() &&
                    state.netAmount.toDoubleOrNull() != null &&
                    isDueDateValid // Agregar validación de fechas (incluyendo fecha actual)
        )
    }

    // Calcular total automáticamente
    LaunchedEffect(state.netAmount, state.taxAmount) {
        val net = state.netAmount.toDoubleOrNull() ?: 0.0
        val tax = state.taxAmount.toDoubleOrNull() ?: 0.0
        val total = net + tax
        // Redondear a 2 decimales para evitar problemas de precisión
        val roundedTotal = kotlin.math.round(total * 100) / 100
        state = state.copy(totalAmount = String.format("%.2f", roundedTotal))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Editar Documento") },
                navigationIcon = {
                    IconButton(onClick = { component.navTo(DocumentsFeatureComponentImpl.Config.DocumentsMain) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    IconButton(onClick = { state = state.copy(showDeleteDialog = true) }) {
                        Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            )
        }
    ) { paddingValues ->
        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            state.document == null && state.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                    ) {
                        Text(
                            text = state.error ?: "Error desconocido",
                            modifier = Modifier.padding(16.dp),
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(onClick = { component.navTo(DocumentsFeatureComponentImpl.Config.DocumentsMain) }) {
                        Text("Volver")
                    }
                }
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Información del documento
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "ID: ${state.document?.id}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Text(
                                text = "Estado: ${state.status}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    // Tipo de documento
                    OutlinedTextField(
                        value = state.documentType,
                        onValueChange = { state = state.copy(documentType = it) },
                        label = { Text("Tipo de Documento") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = state.documentType.isBlank() && state.error != null
                    )

                    // Número de documento
                    OutlinedTextField(
                        value = state.documentNumber,
                        onValueChange = { state = state.copy(documentNumber = it) },
                        label = { Text("Número de Documento") },
                        modifier = Modifier.fillMaxWidth(),
                        isError = state.documentNumber.isBlank() && state.error != null
                    )

                    // Fecha de emisión
                    DatePickerField(
                        value = state.issueDate,
                        onValueChange = { state = state.copy(issueDate = it) },
                        label = "Fecha de Emisión",
                        modifier = Modifier.fillMaxWidth(),
                        isError = state.issueDate == null && state.error != null,
                        supportingText = if (state.issueDate == null && state.error != null) {
                            { Text("La fecha de emisión es obligatoria", color = MaterialTheme.colorScheme.error) }
                        } else null,
                        maxDate = LocalDate(2025, 7, 25) // No permitir fechas futuras más allá de hoy
                    )

                    // Fecha de vencimiento
                    DatePickerField(
                        value = state.dueDate,
                        onValueChange = { 
                            val today = LocalDate(2025, 7, 25) // Fecha actual
                            val minValidDate = if (state.issueDate != null) {
                                if (state.issueDate!! > today) state.issueDate!! else today
                            } else {
                                today
                            }
                            
                            // Validar que la fecha de vencimiento no sea anterior a la fecha de emisión ni a hoy
                            if (it != null && it < minValidDate) {
                                val errorMessage = when {
                                    state.issueDate != null && it < state.issueDate!! -> 
                                        "La fecha de vencimiento no puede ser anterior a la fecha de emisión"
                                    it < today -> 
                                        "La fecha de vencimiento no puede ser anterior a la fecha actual"
                                    else -> null
                                }
                                state = state.copy(error = errorMessage)
                            } else {
                                state = state.copy(dueDate = it, error = null)
                            }
                        },
                        label = "Fecha de Vencimiento (Opcional)",
                        modifier = Modifier.fillMaxWidth(),
                        minDate = run {
                            val today = LocalDate(2025, 7, 25) // Fecha actual
                            if (state.issueDate != null) {
                                if (state.issueDate!! > today) state.issueDate!! else today
                            } else {
                                today
                            }
                        },
                        supportingText = if (state.dueDate != null) {
                            val today = LocalDate(2025, 7, 25)
                            val minValidDate = if (state.issueDate != null) {
                                if (state.issueDate!! > today) state.issueDate!! else today
                            } else {
                                today
                            }
                            
                            when {
                                state.dueDate!! < minValidDate && state.issueDate != null && state.dueDate!! < state.issueDate!! -> {
                                    { Text("La fecha de vencimiento no puede ser anterior a la fecha de emisión", color = MaterialTheme.colorScheme.error) }
                                }
                                state.dueDate!! < today -> {
                                    { Text("La fecha de vencimiento no puede ser anterior a la fecha actual", color = MaterialTheme.colorScheme.error) }
                                }
                                else -> null
                            }
                        } else null,
                        isError = state.dueDate != null && run {
                            val today = LocalDate(2025, 7, 25)
                            val minValidDate = if (state.issueDate != null) {
                                if (state.issueDate!! > today) state.issueDate!! else today
                            } else {
                                today
                            }
                            state.dueDate!! < minValidDate
                        }
                    )

                    // Moneda
                    OutlinedTextField(
                        value = state.currency,
                        onValueChange = { state = state.copy(currency = it) },
                        label = { Text("Moneda") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Monto neto
                        OutlinedTextField(
                            value = state.netAmount,
                            onValueChange = { state = state.copy(netAmount = it) },
                            label = { Text("Monto Neto") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            isError = state.netAmount.toDoubleOrNull() == null && state.netAmount.isNotBlank()
                        )

                        // Impuestos
                        OutlinedTextField(
                            value = state.taxAmount,
                            onValueChange = { state = state.copy(taxAmount = it) },
                            label = { Text("Impuestos") },
                            modifier = Modifier.weight(1f),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal)
                        )
                    }

                    // Total (calculado automáticamente)
                    OutlinedTextField(
                        value = state.totalAmount,
                        onValueChange = { },
                        label = { Text("Total") },
                        modifier = Modifier.fillMaxWidth(),
                        readOnly = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = MaterialTheme.colorScheme.onSurface,
                            disabledBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f),
                            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                        ),
                        enabled = false
                    )

                    // URL del archivo
                    OutlinedTextField(
                        value = state.fileUrl,
                        onValueChange = { state = state.copy(fileUrl = it) },
                        label = { Text("URL del Archivo") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Error message
                    state.error?.let { error ->
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                        ) {
                            Text(
                                text = error,
                                modifier = Modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botones de acción
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { component.navTo(DocumentsFeatureComponentImpl.Config.DocumentsMain) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Text("Cancelar")
                        }

                        Button(
                            onClick = {
                                scope.launch {
                                    state = state.copy(isLoading = true, error = null)
                                    try {
                                        val request = UpdateDocumentRequest(
                                            documentType = state.documentType,
                                            documentNumber = state.documentNumber,
                                            issueDate = state.issueDate!!,
                                            dueDate = state.dueDate,
                                            status = state.status,
                                            currency = state.currency,
                                            netAmount = state.netAmount.toDouble(),
                                            taxAmount = state.taxAmount.toDoubleOrNull() ?: 0.0,
                                            totalAmount = state.totalAmount.toDouble(),
                                            fileUrl = state.fileUrl
                                        )

                                        val result = component.apiClient.documentManagement.updateDocument(
                                            component.documentId,
                                            request
                                        )
                                        if (result != null) {
                                            // Navegar de vuelta a la lista
                                            component.navTo(DocumentsFeatureComponentImpl.Config.DocumentsMain)
                                        } else {
                                            state = state.copy(
                                                isLoading = false,
                                                error = "Error al actualizar el documento"
                                            )
                                        }
                                    } catch (e: Exception) {
                                        state = state.copy(
                                            isLoading = false,
                                            error = "Error: ${e.message}"
                                        )
                                    }
                                }
                            },
                            modifier = Modifier.weight(1f),
                            enabled = state.isValid && !state.isLoading
                        ) {
                            if (state.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            } else {
                                Text("Guardar")
                            }
                        }
                    }
                }
            }
        }
    }

    // Diálogo de confirmación para eliminar
    if (state.showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { state = state.copy(showDeleteDialog = false) },
            title = { Text("Confirmar eliminación") },
            text = { Text("¿Está seguro de que desea eliminar este documento? Esta acción no se puede deshacer.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        scope.launch {
                            state = state.copy(showDeleteDialog = false, isLoading = true)
                            try {
                                val success = component.apiClient.documentManagement.deleteDocument(component.documentId)
                                if (success) {
                                    // Navegar de vuelta a la lista
                                    component.navTo(DocumentsFeatureComponentImpl.Config.DocumentsMain)
                                } else {
                                    state = state.copy(
                                        isLoading = false,
                                        error = "Error al eliminar el documento"
                                    )
                                }
                            } catch (e: Exception) {
                                state = state.copy(
                                    isLoading = false,
                                    error = "Error: ${e.message}"
                                )
                            }
                        }
                    }
                ) {
                    Text("Eliminar", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { state = state.copy(showDeleteDialog = false) }) {
                    Text("Cancelar")
                }
            }
        )
    }
}
