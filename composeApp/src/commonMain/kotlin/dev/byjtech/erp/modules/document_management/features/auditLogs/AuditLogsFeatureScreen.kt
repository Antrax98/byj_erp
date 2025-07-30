package dev.byjtech.erp.modules.document_management.features.auditLogs

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import dev.byjtech.erp.document_management.dto.DocumentAuditLogDTO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditLogsFeatureScreen(component: AuditLogsFeatureComponent) {
    Children(stack = component.childStack) { child ->
        when (val instance = child.instance) {
            is AuditLogsFeatureComponent.Child.Main -> {
                AuditLogsMainScreen(instance.component)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuditLogsMainScreen(component: AuditLogsMainComponent) {
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
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Logs de Auditoría",
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
            
            state.auditLogs.isEmpty() -> {
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
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.outline
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No hay logs de auditoría",
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
                    items(state.auditLogs) { auditLog ->
                        AuditLogCard(auditLog)
                    }
                }
            }
        }
    }
}

@Composable
fun AuditLogCard(auditLog: DocumentAuditLogDTO) {
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
                    // Tipo de evento con badge
                    Surface(
                        color = getEventTypeColor(auditLog.eventType),
                        shape = MaterialTheme.shapes.small
                    ) {
                        Text(
                            text = getEventTypeDisplayName(auditLog.eventType),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Text(
                        text = auditLog.description,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                
                Column(
                    horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = formatDate(auditLog.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                    Text(
                        text = formatTime(auditLog.createdAt),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))
            Divider()
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Documento ID: ${auditLog.documentId}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
private fun getEventTypeColor(eventType: String): androidx.compose.ui.graphics.Color {
    return when (eventType) {
        "CREATED" -> MaterialTheme.colorScheme.primary
        "UPDATED" -> MaterialTheme.colorScheme.secondary
        "DELETED" -> MaterialTheme.colorScheme.error
        "DEACTIVATED" -> MaterialTheme.colorScheme.tertiary
        "REACTIVATED" -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.outline
    }
}

@Composable
private fun getEventTypeDisplayName(eventType: String): String {
    return when (eventType) {
        "CREATED" -> "Creado"
        "UPDATED" -> "Actualizado"
        "DELETED" -> "Eliminado"
        "DEACTIVATED" -> "Desactivado"
        "REACTIVATED" -> "Reactivado"
        else -> eventType
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
