package dev.byjtech.erp.modules.document_management.features.documents.nav.documentsMain

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.document_management.dto.DocumentDTO
import dev.byjtech.erp.modules.document_management.domain.model.DocumentStatus
import dev.byjtech.erp.modules.document_management.utils.getStatusDisplayName
import dev.byjtech.erp.modules.document_management.features.documents.components.DocumentSearchFilters
import dev.byjtech.erp.modules.document_management.features.documents.components.DocumentPagination
import dev.byjtech.erp.modules.document_management.request.DocumentSearchRequest
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentsMainScreen(component: DocumentsMainComponent) {
    val state by component.state.collectAsState()
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header con título y botones de acción
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Documentos",
                style = MaterialTheme.typography.headlineMedium
            )
            
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Botón para alternar modo búsqueda
                IconButton(
                    onClick = { component.toggleSearchMode() }
                ) {
                    Icon(
                        imageVector = if (state.isSearchMode) Icons.Default.List else Icons.Default.Search,
                        contentDescription = if (state.isSearchMode) "Ver todos" else "Buscar"
                    )
                }
                
                // Botón agregar documento
                FloatingActionButton(
                    onClick = { component.onAddDocumentClick() }
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Documento")
                }
            }
        }

        // Filtros de búsqueda (solo en modo búsqueda)
        if (state.isSearchMode) {
            DocumentSearchFilters(
                searchRequest = state.currentSearchRequest,
                onSearchRequestChange = { searchRequest ->
                    component.updateSearchRequest(searchRequest)
                },
                onSearch = {
                    scope.launch {
                        component.searchDocuments(state.currentSearchRequest)
                    }
                },
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        when {
            state.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            
            state.error != null -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.errorContainer)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Error",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = state.error ?: "Error desconocido",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                scope.launch {
                                    if (state.isSearchMode) {
                                        component.searchDocuments(state.currentSearchRequest)
                                    } else {
                                        component.loadDocuments()
                                    }
                                }
                            }
                        ) {
                            Text("Reintentar")
                        }
                    }
                }
            }
            
            state.documents.isEmpty() -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = if (state.isSearchMode) "No se encontraron documentos" else "No hay documentos",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        if (state.isSearchMode) {
                            Spacer(modifier = Modifier.height(8.dp))
                            TextButton(
                                onClick = { component.toggleSearchMode() }
                            ) {
                                Text("Ver todos los documentos")
                            }
                        }
                    }
                }
            }
            
            else -> {
                // Lista de documentos
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(state.documents, key = { it.id }) { document ->
                        DocumentCard(
                            document = document,
                            onClick = { component.onDocumentClick(document.id) }
                        )
                    }
                }
                
                // Paginación (solo en modo búsqueda)
                state.searchResponse?.let { searchResponse ->
                    DocumentPagination(
                        searchResponse = searchResponse,
                        onPageChange = { page -> component.onPageChange(page) },
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun DocumentCard(
    document: DocumentDTO,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "${document.documentType} - ${document.documentNumber}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Fecha: ${document.issueDate}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Surface(
                    color = when (document.status) {
                        DocumentStatus.UPLOADED -> MaterialTheme.colorScheme.primaryContainer
                        DocumentStatus.SENT -> MaterialTheme.colorScheme.secondaryContainer
                        DocumentStatus.APPROVED -> MaterialTheme.colorScheme.tertiaryContainer
                        DocumentStatus.REJECTED -> MaterialTheme.colorScheme.errorContainer
                    },
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        text = getStatusDisplayName(document.status),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        style = MaterialTheme.typography.labelMedium
                    )
                }
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Total: ${document.totalAmount} ${document.currency}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                if (document.dueDate != null) {
                    Text(
                        text = "Vence: ${document.dueDate}",
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}
