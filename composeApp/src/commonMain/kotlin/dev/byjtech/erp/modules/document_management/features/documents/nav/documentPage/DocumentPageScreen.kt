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
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.document_management.dto.DocumentDTO
import dev.byjtech.erp.modules.document_management.api.DocumentManagementClient
import dev.byjtech.erp.modules.document_management.domain.model.getDocumentTypeDisplayName
import dev.byjtech.erp.modules.document_management.utils.getAvailableActions
import dev.byjtech.erp.modules.document_management.utils.getStatusDisplayName
import dev.byjtech.erp.modules.document_management.utils.DocumentActions
import dev.byjtech.erp.modules.document_management.utils.canBeDeactivated
import dev.byjtech.erp.modules.document_management.utils.getDeactivationRestrictionMessage
import dev.byjtech.erp.modules.document_management.features.documents.DocumentsFeatureComponentImpl
import dev.byjtech.erp.document_management.request.DeactivateDocumentRequest
import dev.byjtech.erp.common.ApiResponse
import kotlinx.coroutines.launch

data class DocumentPageState(
    val isLoading: Boolean = false,
    val document: DocumentDTO? = null,
    val error: String? = null,
    val showDeactivateDialog: Boolean = false,
    val deactivateReason: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentPageScreen(component: DocumentPageComponent) {
    var state by remember { mutableStateOf(DocumentPageState()) }
    val scope = rememberCoroutineScope()
    val scrollState = rememberScrollState()
    val uriHandler = LocalUriHandler.current

    //se crean estados de tooltip
    val backTooltipState = rememberTooltipState()
    val sendTooltipState = rememberTooltipState()
    val approveTooltipState = rememberTooltipState()
    val rejectTooltipState = rememberTooltipState()
    val editTooltipState = rememberTooltipState()
    val deactivateTooltipState = rememberTooltipState()
    val openFileTooltipState = rememberTooltipState()
    val errorBackTooltipState = rememberTooltipState()
    val editButtonTooltipState = rememberTooltipState()
    val viewFileButtonTooltipState = rememberTooltipState()
    val confirmTooltipState = rememberTooltipState()
    val cancelTooltipState = rememberTooltipState()

    //se carga el documento al inicializar la pantalla
    LaunchedEffect(component.documentId) {
        state = state.copy(isLoading = true)
        try {
            val documentManagementClient = DocumentManagementClient(component.apiClient.clientKtor)
            val document: DocumentDTO? = documentManagementClient.getDocumentById(component.documentId)
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
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip {
                                Text("Volver")
                            }
                        },
                        state = backTooltipState
                    ) {
                        IconButton(onClick = { component.onNavigateBack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                        }
                    }
                },
                actions = {
                    state.document?.let { document ->
                        val actions = getAvailableActions(document.status, "USER")
                        
                        //se muestra botón para enviar documento
                        if (actions.canSend) {
                            TooltipBox(
                                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                tooltip = {
                                    PlainTooltip {
                                        Text("Enviar documento")
                                    }
                                },
                                state = sendTooltipState
                            ) {
                                IconButton(
                                    onClick = { 
                                        scope.launch {
                                            state = state.copy(isLoading = true)
                                            val documentManagementClient = DocumentManagementClient(component.apiClient.clientKtor)
                                            val result = documentManagementClient.changeDocumentStatus(
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
                        }
                        
                        // Botón para aprobar (SENT -> APPROVED)
                        if (actions.canApprove) {
                            TooltipBox(
                                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                tooltip = {
                                    PlainTooltip {
                                        Text("Aprobar documento")
                                    }
                                },
                                state = approveTooltipState
                            ) {
                                IconButton(
                                    onClick = { 
                                        scope.launch {
                                            state = state.copy(isLoading = true)
                                            val documentManagementClient = DocumentManagementClient(component.apiClient.clientKtor)
                                            val result = documentManagementClient.changeDocumentStatus(
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
                        }
                        
                        // Botón para rechazar (SENT -> REJECTED)
                        if (actions.canReject) {
                            TooltipBox(
                                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                tooltip = {
                                    PlainTooltip {
                                        Text("Rechazar documento")
                                    }
                                },
                                state = rejectTooltipState
                            ) {
                                IconButton(
                                    onClick = { 
                                        scope.launch {
                                            state = state.copy(isLoading = true)
                                            val documentManagementClient = DocumentManagementClient(component.apiClient.clientKtor)
                                            val result = documentManagementClient.changeDocumentStatus(
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
                        }
                        
                        // Botón para editar (solo si se puede editar)
                        if (actions.canEdit) {
                            TooltipBox(
                                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                tooltip = {
                                    PlainTooltip {
                                        Text("Editar documento")
                                    }
                                },
                                state = editTooltipState
                            ) {
                                IconButton(onClick = { component.onEditDocument(document.id) }) {
                                    Icon(Icons.Default.Edit, contentDescription = "Editar")
                                }
                            }
                        }
                        
                        // Botón para desactivar (solo si se puede desactivar)
                        if (canBeDeactivated(document.status)) {
                            TooltipBox(
                                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                tooltip = {
                                    PlainTooltip {
                                        Text("Desactivar documento")
                                    }
                                },
                                state = deactivateTooltipState
                            ) {
                                IconButton(onClick = { state = state.copy(showDeactivateDialog = true) }) {
                                    Icon(Icons.Default.RemoveCircle, contentDescription = "Desactivar")
                                }
                            }
                        }
                        
                        // Botón para abrir archivo (si existe URL)
                        if (document.fileUrl.isNotBlank()) {
                            TooltipBox(
                                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                tooltip = {
                                    PlainTooltip {
                                        Text("Abrir archivo")
                                    }
                                },
                                state = openFileTooltipState
                            ) {
                                IconButton(onClick = { uriHandler.openUri(document.fileUrl) }) {
                                    Icon(Icons.Default.OpenInBrowser, contentDescription = "Abrir archivo")
                                }
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
                    TooltipBox(
                        positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                        tooltip = {
                            PlainTooltip {
                                Text("Volver a la lista de documentos")
                            }
                        },
                        state = errorBackTooltipState
                    ) {
                        Button(onClick = { component.onNavigateBack() }) {
                            Text("Volver")
                        }
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
                        TooltipBox(
                            positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                            tooltip = {
                                PlainTooltip {
                                    Text("Editar documento")
                                }
                            },
                            state = editButtonTooltipState
                        ) {
                            OutlinedButton(
                                onClick = { component.onEditDocument(document.id) },
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(Icons.Default.Edit, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Editar")
                            }
                        }
                        
                        if (document.fileUrl.isNotBlank()) {
                            TooltipBox(
                                positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                                tooltip = {
                                    PlainTooltip {
                                        Text("Abrir archivo en el navegador")
                                    }
                                },
                                state = viewFileButtonTooltipState
                            ) {
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
    }
    
    // Diálogo de confirmación para desactivar
    if (state.showDeactivateDialog) {
        AlertDialog(
            onDismissRequest = { state = state.copy(showDeactivateDialog = false, deactivateReason = "") },
            title = { Text("Confirmar desactivación") },
            text = { 
                Column {
                    Text("¿Está seguro de que desea desactivar este documento?")
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "El documento será marcado como inactivo pero se mantendrá en el sistema para auditoría. Esta acción se puede revertir.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedTextField(
                        value = state.deactivateReason,
                        onValueChange = { state = state.copy(deactivateReason = it) },
                        label = { Text("Razón de desactivación (opcional)") },
                        modifier = Modifier.fillMaxWidth(),
                        maxLines = 3
                    )
                }
            },
            confirmButton = {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip {
                            Text("Confirmar desactivación del documento")
                        }
                    },
                    state = confirmTooltipState
                ) {
                    TextButton(
                        onClick = {
                            scope.launch {
                            state = state.copy(showDeactivateDialog = false, isLoading = true)
                            try {
                                val deactivateRequest = DeactivateDocumentRequest(
                                    confirm = true,
                                    reason = state.deactivateReason.takeIf { it.isNotBlank() }
                                )
                                val documentManagementClient = DocumentManagementClient(component.apiClient.clientKtor)
                                val result = documentManagementClient.deactivateDocument(
                                    component.documentId, 
                                    deactivateRequest
                                )
                                when (result) {
                                    is ApiResponse.Success -> {
                                        // Navegar de vuelta a la lista y recargarla
                                        component.onDocumentDeactivated()
                                    }
                                    is ApiResponse.Error -> {
                                        state = state.copy(
                                            isLoading = false,
                                            error = result.code ?: "Error al desactivar el documento",
                                            deactivateReason = ""
                                        )
                                    }
                                }
                            } catch (e: Exception) {
                                state = state.copy(
                                    isLoading = false,
                                    error = "Error: ${e.message}",
                                    deactivateReason = ""
                                )
                            }
                        }
                    }
                ) {
                    Text("Desactivar", color = MaterialTheme.colorScheme.error)
                }
                }
            },
            dismissButton = {
                TooltipBox(
                    positionProvider = TooltipDefaults.rememberPlainTooltipPositionProvider(),
                    tooltip = {
                        PlainTooltip {
                            Text("Cancelar desactivación")
                        }
                    },
                    state = cancelTooltipState
                ) {
                    TextButton(onClick = { state = state.copy(showDeactivateDialog = false, deactivateReason = "") }) {
                        Text("Cancelar")
                    }
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
