package dev.byjtech.erp.modules.document_management.features.documents.nav.editDocument

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
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
                    status = document.status
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
    LaunchedEffect(state.documentType, state.documentNumber, state.issueDate, state.netAmount) {
        state = state.copy(
            isValid = state.documentType.isNotBlank() &&
                    state.documentNumber.isNotBlank() &&
                    state.issueDate != null &&
                    state.netAmount.isNotBlank() &&
                    state.netAmount.toDoubleOrNull() != null
        )
    }

    // Calcular total automáticamente
    LaunchedEffect(state.netAmount, state.taxAmount) {
        val net = state.netAmount.toDoubleOrNull() ?: 0.0
        val tax = state.taxAmount.toDoubleOrNull() ?: 0.0
        state = state.copy(totalAmount = (net + tax).toString())
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
                    OutlinedTextField(
                        value = state.issueDate?.toString() ?: "",
                        onValueChange = { 
                            try {
                                state = state.copy(issueDate = LocalDate.parse(it))
                            } catch (e: Exception) {
                                // Manejar error de fecha inválida
                            }
                        },
                        label = { Text("Fecha de Emisión") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("YYYY-MM-DD") },
                        trailingIcon = {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Seleccionar fecha")
                        },
                        isError = state.issueDate == null && state.error != null
                    )

                    // Fecha de vencimiento
                    OutlinedTextField(
                        value = state.dueDate?.toString() ?: "",
                        onValueChange = { 
                            try {
                                if (it.isBlank()) {
                                    state = state.copy(dueDate = null)
                                } else {
                                    state = state.copy(dueDate = LocalDate.parse(it))
                                }
                            } catch (e: Exception) {
                                // Manejar error de fecha inválida
                            }
                        },
                        label = { Text("Fecha de Vencimiento (Opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("YYYY-MM-DD") },
                        trailingIcon = {
                            Icon(Icons.Default.CalendarToday, contentDescription = "Seleccionar fecha")
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
