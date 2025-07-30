package dev.byjtech.erp.modules.document_management.features.editHistory

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import dev.byjtech.erp.document_management.dto.DocumentEditHistoryDTO
import dev.byjtech.erp.modules.document_management.utils.getStatusDisplayName
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHistoryFeatureScreen(component: EditHistoryFeatureComponent) {
    Children(stack = component.childStack) { child ->
        when (val instance = child.instance) {
            is EditHistoryFeatureComponent.Child.Main -> {
                EditHistoryMainScreen(instance.component)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditHistoryMainScreen(component: EditHistoryMainComponent) {
    val state by component.state.collectAsState()
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Header con título y botón de refrescar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.History,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Historial de Ediciones",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
            }
            
            IconButton(
                onClick = { component.refresh() }
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Refrescar"
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Contenido
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
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Text(
                        text = state.error ?: "Error desconocido",
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                }
            }
            
            state.editHistory.isEmpty() -> {
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No hay historial de ediciones",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
            
            else -> {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.editHistory) { editEntry ->
                        EditHistoryCard(editEntry)
                    }
                }
            }
        }
    }
}

@Composable
fun EditHistoryCard(editEntry: DocumentEditHistoryDTO) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Campo: ${editEntry.fieldName}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Mostrar cambio de valor
                    Row {
                        Text(
                            text = "De: ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = getDisplayValue(editEntry.fieldName, editEntry.oldValue ?: "N/A"),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                    
                    Row {
                        Text(
                            text = "A: ",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.outline
                        )
                        Text(
                            text = getDisplayValue(editEntry.fieldName, editEntry.newValue ?: "N/A"),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = formatDate(editEntry.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = formatTime(editEntry.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Documento ID: ${editEntry.documentId}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun getDisplayValue(fieldName: String, value: String): String {
    return when (fieldName) {
        "status" -> {
            try {
                getStatusDisplayName(dev.byjtech.erp.modules.document_management.domain.model.DocumentStatus.valueOf(value))
            } catch (e: Exception) {
                value
            }
        }
        "type" -> {
            when (value) {
                "INVOICE" -> "Factura"
                "RECEIPT" -> "Boleta"
                "CREDIT_NOTE" -> "Nota de Crédito"
                "DEBIT_NOTE" -> "Nota de Débito"
                else -> value
            }
        }
        else -> value
    }
}

// Función para formatear fecha
fun formatDate(dateTime: kotlinx.datetime.LocalDateTime): String {
    return try {
        val str = dateTime.toString() // Formato: 2023-12-31T14:30:00
        val datePart = str.split('T')[0] // 2023-12-31
        
        // Reformatear de yyyy-mm-dd a dd/mm/yyyy
        val parts = datePart.split('-')
        "${parts[2]}/${parts[1]}/${parts[0]}"
    } catch (e: Exception) {
        "N/A"
    }
}

// Función para formatear hora
fun formatTime(dateTime: kotlinx.datetime.LocalDateTime): String {
    return try {
        val str = dateTime.toString() // Formato: 2023-12-31T14:30:00
        val timePart = str.split('T').getOrNull(1)?.take(5) ?: "00:00" // 14:30
        timePart
    } catch (e: Exception) {
        "N/A"
    }
}
