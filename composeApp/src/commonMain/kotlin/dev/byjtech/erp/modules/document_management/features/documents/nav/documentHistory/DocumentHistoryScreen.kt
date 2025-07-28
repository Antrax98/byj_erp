package dev.byjtech.erp.modules.document_management.features.documents.nav.documentHistory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.document_management.dto.DocumentEditHistoryDTO
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DocumentHistoryScreen(component: DocumentHistoryComponent) {
    val state by component.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Top App Bar
        TopAppBar(
            title = { 
                Text("Historial de Edición") 
            },
            navigationIcon = {
                IconButton(onClick = { component.onBack() }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                }
            },
            actions = {
                IconButton(onClick = { component.refreshHistory() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Actualizar")
                }
            }
        )

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
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Error",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = state.error ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                    }
                }
            }

            state.historyEntries.isEmpty() -> {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Sin historial de edición",
                            style = MaterialTheme.typography.titleMedium
                        )
                        Text(
                            text = "Este documento no tiene cambios registrados",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }

            else -> {
                Column {
                    // Resumen de cambios
                    if (state.summary.isNotEmpty()) {
                        SummaryCard(summary = state.summary)
                    }

                    // Lista de cambios
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(16.dp)
                    ) {
                        items(state.historyEntries, key = { it.id }) { entry ->
                            HistoryEntryCard(entry = entry)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SummaryCard(summary: Map<String, Any>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Resumen de Cambios",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                SummaryItem(
                    label = "Total de Cambios",
                    value = summary["totalChanges"]?.toString() ?: "0"
                )
                SummaryItem(
                    label = "Editores Únicos", 
                    value = summary["uniqueEditors"]?.toString() ?: "0"
                )
            }

            val modifiedFields = summary["modifiedFields"] as? List<*>
            if (!modifiedFields.isNullOrEmpty()) {
                Text(
                    text = "Campos modificados:",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = modifiedFields.joinToString(", ") { getFieldDisplayName(it.toString()) },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@Composable
fun SummaryItem(label: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun HistoryEntryCard(entry: DocumentEditHistoryDTO) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Header con campo y fecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = getFieldDisplayName(entry.fieldName),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(12.dp)
                    )
                    Text(
                        text = formatDateTime(entry.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }

            // Cambio de valor
            if (entry.oldValue != null || entry.newValue != null) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    if (entry.oldValue != null) {
                        Text(
                            text = "Valor anterior: ${entry.oldValue}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                    if (entry.newValue != null) {
                        Text(
                            text = "Nuevo valor: ${entry.newValue}",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Usuario editor
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = "Editado por: ${entry.userId}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    }
}

// Función para formatear fecha y hora
fun formatDateTime(dateTime: LocalDateTime): String {
    return try {
        // Usar toString() y formatear la salida
        val str = dateTime.toString() // Formato: 2023-12-31T14:30:00
        val parts = str.split('T')
        val datePart = parts[0] // 2023-12-31
        val timePart = parts.getOrNull(1)?.take(5) ?: "00:00" // 14:30
        
        // Reformatear de yyyy-mm-dd a dd/mm/yyyy
        val dateComponents = datePart.split('-')
        if (dateComponents.size == 3) {
            "${dateComponents[2]}/${dateComponents[1]}/${dateComponents[0]} $timePart"
        } else {
            dateTime.toString()
        }
    } catch (e: Exception) {
        dateTime.toString()
    }
}

// Función para convertir nombres de campos técnicos a nombres amigables
fun getFieldDisplayName(fieldName: String): String {
    return when (fieldName) {
        "type" -> "Tipo de Documento"
        "documentNumber" -> "Número de Documento"
        "issueDate" -> "Fecha de Emisión"
        "dueDate" -> "Fecha de Vencimiento"
        "currency" -> "Moneda"
        "netAmount" -> "Monto Neto"
        "taxAmount" -> "Monto de Impuestos"
        "totalAmount" -> "Monto Total"
        "fileUrl" -> "Archivo URL"
        "status" -> "Estado"
        "active" -> "Activo"
        else -> fieldName.replaceFirstChar { it.uppercase() }
    }
}
