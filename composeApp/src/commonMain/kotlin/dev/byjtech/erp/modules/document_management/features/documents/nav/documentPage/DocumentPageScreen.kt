package dev.byjtech.erp.modules.document_management.features.documents.nav.documentPage

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.document_management.dto.DocumentDTO
import dev.byjtech.erp.modules.document_management.domain.model.getDocumentTypeDisplayName
import dev.byjtech.erp.modules.document_management.utils.getAvailableActions
import dev.byjtech.erp.modules.document_management.utils.getStatusDisplayName
import dev.byjtech.erp.modules.document_management.utils.DocumentActions
import dev.byjtech.erp.modules.document_management.features.documents.DocumentsFeatureComponentImpl
import kotlinx.coroutines.launch

data class DocumentPageState(
    val isLoading: Boolean = false,
    val document: DocumentDTO? = null,
    val error: String? = null,
    val showDeleteDialog: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentPageScreen(component: DocumentPageComponent) {
    var state by remember { mutableStateOf(DocumentPageState()) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    // Cargar documento al inicializar
    LaunchedEffect(component.documentId) {
        state = state.copy(isLoading = true)
        try {
            val document: DocumentDTO? = component.apiClient.documentManagement.getDocumentById(component.documentId)
            if (document != null) {
                state = state.copy(
                    isLoading = false,
                    document = document
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalles del Documento") },
                navigationIcon = {
                    IconButton(onClick = { component.navTo(DocumentsFeatureComponentImpl.Config.DocumentsMain) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                actions = {
                    state.document?.let { document ->
                        val actions = getAvailableActions(document.status, "USER") // Por ahora asumimos USER
                        
                        // Botón para enviar (UPLOADED -> SENT)
                        if (actions.canSend) {
                            IconButton(
                                onClick = { 
                                    scope.launch {
                                        state = state.copy(isLoading = true)
                                        val result = component.apiClient.documentManagement.changeDocumentStatus(
                                            document.id, 
                                            "SENT"
                                        )
                                        if (result != null) {
                                            state = state.copy(document = result, isLoading = false)
                                        } else {
                                            state = state.copy(
                                                isLoading = false, 
                                                error = "Error al enviar documento"
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Send, contentDescription = "Enviar")
                            }
                        }
                        
                        // Botón para aprobar (SENT -> APPROVED)
                        if (actions.canApprove) {
                            IconButton(
                                onClick = { 
                                    scope.launch {
                                        state = state.copy(isLoading = true)
                                        val result = component.apiClient.documentManagement.changeDocumentStatus(
                                            document.id, 
                                            "APPROVED"
                                        )
                                        if (result != null) {
                                            state = state.copy(document = result, isLoading = false)
                                        } else {
                                            state = state.copy(
                                                isLoading = false, 
                                                error = "Error al aprobar documento"
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Check, contentDescription = "Aprobar")
                            }
                        }
                        
                        // Botón para rechazar (SENT -> REJECTED)
                        if (actions.canReject) {
                            IconButton(
                                onClick = { 
                                    scope.launch {
                                        state = state.copy(isLoading = true)
                                        val result = component.apiClient.documentManagement.changeDocumentStatus(
                                            document.id, 
                                            "REJECTED"
                                        )
                                        if (result != null) {
                                            state = state.copy(document = result, isLoading = false)
                                        } else {
                                            state = state.copy(
                                                isLoading = false, 
                                                error = "Error al rechazar documento"
                                            )
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Rechazar")
                            }
                        }
                        
                        // Botón para editar (solo si se puede editar)
                        if (actions.canEdit) {
                            IconButton(onClick = { component.onEditDocument(document.id) }) {
                                Icon(Icons.Default.Edit, contentDescription = "Editar")
                            }
                        }
                        
                        // Botón para eliminar (solo si se puede eliminar)
                        if (actions.canDelete) {
                            IconButton(onClick = { state = state.copy(showDeleteDialog = true) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
                            }
                        }
                        
                        // Botón para abrir archivo (si existe URL)
                        if (document.fileUrl.isNotBlank()) {
                            IconButton(onClick = { uriHandler.openUri(document.fileUrl) }) {
                                Icon(Icons.Default.OpenInBrowser, contentDescription = "Abrir archivo")
                            }
                        }
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
            state.document != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(16.dp)
                        .verticalScroll(scrollState),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    val document = state.document!!
                    
                    // Información básica
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = getDocumentTypeDisplayName(document.type),
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = document.documentNumber,
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                            Text(
                                text = "Estado: ${getStatusDisplayName(document.status)}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    // Fechas
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Fechas",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            DetailRow("Fecha de Emisión", document.issueDate.toString())
                            document.dueDate?.let { dueDate ->
                                DetailRow("Fecha de Vencimiento", dueDate.toString())
                            }
                        }
                    }

                    // Montos
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Montos",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            DetailRow("Moneda", document.currency)
                            DetailRow("Monto Neto", "${document.currency} ${document.netAmount}")
                            DetailRow("Impuestos", "${document.currency} ${document.taxAmount}")
                            
                            Divider(modifier = Modifier.padding(vertical = 8.dp))
                            
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(
                                    text = "Total",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    text = "${document.currency} ${document.totalAmount}",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    // Información adicional
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Información Adicional",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            
                            DetailRow("ID del Documento", document.id)
                            DetailRow("Creado por", document.createdBy)
                            DetailRow("Fecha de Creación", document.createdAt.toString())
                            DetailRow("Última Actualización", document.updatedAt.toString())
                            if (document.fileUrl.isNotBlank()) {
                                DetailRow("Archivo", document.fileUrl)
                            }
                            DetailRow("Activo", if (document.active) "Sí" else "No")
                        }
                    }

                    // Botones de acción
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = { component.onEditDocument(document.id) },
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(Icons.Default.Edit, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Editar")
                        }
                        
                        if (document.fileUrl.isNotBlank()) {
                            Button(
                                onClick = { uriHandler.openUri(document.fileUrl) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.OpenInBrowser, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Ver Archivo")
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

@Composable
private fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
    }
}
