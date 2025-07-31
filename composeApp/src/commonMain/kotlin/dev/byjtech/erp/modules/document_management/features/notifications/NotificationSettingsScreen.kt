package dev.byjtech.erp.modules.document_management.features.notifications

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.document_management.dto.UserNotificationSettingsDTO
import dev.byjtech.erp.document_management.request.UpdateNotificationSettingsRequest

data class NotificationSettingsUiState(
    val settings: UserNotificationSettingsDTO? = null,
    val daysBeforeExpiration: Int = 7,
    val emailEnabled: Boolean = true,
    val systemEnabled: Boolean = true,
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotificationSettingsScreen(
    state: NotificationSettingsUiState,
    onLoadSettings: () -> Unit,
    onUpdateSettings: (UpdateNotificationSettingsRequest) -> Unit,
    onDaysChanged: (Int) -> Unit,
    onEmailEnabledChanged: (Boolean) -> Unit,
    onSystemEnabledChanged: (Boolean) -> Unit,
    onDismissError: () -> Unit,
    onDismissSuccess: () -> Unit
) {
    LaunchedEffect(Unit) {
        onLoadSettings()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            text = "Configuración de Notificaciones",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = "Configura cuándo quieres recibir notificaciones sobre documentos próximos a vencer",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Days before expiration
                    OutlinedTextField(
                        value = state.daysBeforeExpiration.toString(),
                        onValueChange = { value ->
                            value.toIntOrNull()?.let { days ->
                                if (days in 1..365) {
                                    onDaysChanged(days)
                                }
                            }
                        },
                        label = { Text("Días de anticipación") },
                        supportingText = { 
                            Text("Entre 1 y 365 días antes del vencimiento")
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true
                    )

                    // Email notifications
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Notificaciones por email",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Recibir alertas por correo electrónico",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = state.emailEnabled,
                            onCheckedChange = onEmailEnabledChanged
                        )
                    }

                    // System notifications
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Notificaciones del sistema",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "Mostrar notificaciones en la aplicación",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = state.systemEnabled,
                            onCheckedChange = onSystemEnabledChanged
                        )
                    }

                    // Save button
                    Button(
                        onClick = {
                            onUpdateSettings(
                                UpdateNotificationSettingsRequest(
                                    daysBeforeExpiration = state.daysBeforeExpiration,
                                    emailEnabled = state.emailEnabled,
                                    systemEnabled = state.systemEnabled
                                )
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !state.isLoading
                    ) {
                        Text("Guardar Configuración")
                    }
                }
            }
        }

        // Success message
        state.successMessage?.let { message ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    TextButton(onClick = onDismissSuccess) {
                        Text("OK")
                    }
                }
            }
        }

        // Error message
        state.error?.let { error ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = error,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onErrorContainer
                    )
                    TextButton(onClick = onDismissError) {
                        Text("OK")
                    }
                }
            }
        }
    }
}
