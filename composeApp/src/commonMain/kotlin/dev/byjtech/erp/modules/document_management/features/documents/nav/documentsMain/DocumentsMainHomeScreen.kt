package dev.byjtech.erp.modules.document_management.features.documents.nav.documentsMain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.modules.document_management.features.documents.DocumentsFeatureComponentImpl.Config

@Composable
fun DocumentsMainHomeScreen(component: DocumentsMainHomeComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                NavigationCard(
                    title = "Ver Documentos",
                    subtitle = "Consultar todos los documentos del sistema",
                    icon = Icons.Default.List
                ) {
                    component.navTo(Config.DocumentsMain)
                }
            }

            item {
                NavigationCard(
                    title = "Crear Documento",
                    subtitle = "Agregar un nuevo documento",
                    icon = Icons.Default.Add
                ) {
                    component.navTo(Config.AddDocument)
                }
            }

            item {
                NavigationCard(
                    title = "Buscar Documentos",
                    subtitle = "Búsqueda avanzada de documentos",
                    icon = Icons.Default.Search
                ) {
                    // TODO: Implementar pantalla de búsqueda avanzada
                    println("Buscar Documentos")
                }
            }

            item {
                NavigationCard(
                    title = "Notificaciones",
                    subtitle = "Ver notificaciones de documentos",
                    icon = Icons.Default.Notifications
                ) {
                    component.navTo(Config.Notifications)
                }
            }

            item {
                NavigationCard(
                    title = "Configuración",
                    subtitle = "Configurar notificaciones",
                    icon = Icons.Default.Settings
                ) {
                    component.navTo(Config.NotificationSettings)
                }
            }

            item {
                NavigationCard(
                    title = "Reportes",
                    subtitle = "Generar reportes de documentos",
                    icon = Icons.Default.Description
                ) {
                    // TODO: Implementar pantalla de reportes
                    println("Reportes")
                }
            }

            item {
                NavigationCard(
                    title = "Auditoría",
                    subtitle = "Ver registros de auditoría",
                    icon = Icons.Default.History
                ) {
                    // TODO: Navegar a auditoría si existe
                    println("Auditoría")
                }
            }

            item {
                NavigationCard(
                    title = "Estadísticas",
                    subtitle = "Ver estadísticas del sistema",
                    icon = Icons.Default.Description
                ) {
                    // TODO: Implementar pantalla de estadísticas
                    println("Estadísticas")
                }
            }
        }
    }
}

@Composable
fun NavigationCard(
    title: String,
    subtitle: String? = null,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Card Icon",
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            subtitle?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Ir",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
