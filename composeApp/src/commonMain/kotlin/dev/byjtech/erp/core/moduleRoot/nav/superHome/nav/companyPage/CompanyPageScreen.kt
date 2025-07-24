package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companyPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ReceiptLong
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.common.UnderConstructionScreen
import dev.byjtech.erp.core.dto.CompanyDTO
import dev.byjtech.erp.core.dto.ModuleDTO
import dev.byjtech.erp.core.dto.SubscriptionDTO
import dev.byjtech.erp.core.moduleRoot.nav.superHome.SuperHomeComponentImpl
import kotlinx.coroutines.flow.StateFlow

@Composable
fun CompanyPageScreen(component: CompanyPageComponent) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item { CompanyInfoCard(company = component.company, component) }
        item { BillingInfoCard() }
        item { SubscriptionsList(component.subsModMap, onAddClick = { component.navToAddSubscription(component.company) }, onToggleAccess = { component.changeSubscriptionAccess(it.id, !it.isAccessible) })  }
    }
}


@Composable
fun CompanyInfoCard(company: CompanyDTO, component: CompanyPageComponent) {
    val showEditNameDialog = remember { mutableStateOf(false) }
    val showEditEmailDialog = remember { mutableStateOf(false) }

    val nameInput = remember { mutableStateOf(company.name) }
    val emailInput = remember { mutableStateOf(company.contactEmail) }


    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp)) {
            Icon(
                imageVector = Icons.Default.Business,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(64.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
                    .padding(16.dp)
            )

            Spacer(Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                // Nombre con botón
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = company.name,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { showEditNameDialog.value = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar nombre",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // Email con botón
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = company.contactEmail,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { showEditEmailDialog.value = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Enviar email",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                // RUT sin botón
                Text(
                    text = "RUT: ${company.rut}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }

    if (showEditNameDialog.value) {
        AlertDialog(
            onDismissRequest = { showEditNameDialog.value = false },
            title = { Text("Editar nombre de empresa") },
            text = {
                TextField(
                    value = nameInput.value,
                    onValueChange = { nameInput.value = it },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    component.updateCompanyName(nameInput.value)
                    showEditNameDialog.value = false
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditNameDialog.value = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    if (showEditEmailDialog.value) {
        AlertDialog(
            onDismissRequest = { showEditEmailDialog.value = false },
            title = { Text("Editar correo de contacto") },
            text = {
                TextField(
                    value = emailInput.value,
                    onValueChange = { emailInput.value = it },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    component.updateCompanyEmail(emailInput.value)
                    showEditEmailDialog.value = false
                }) {
                    Text("Guardar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditEmailDialog.value = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

}


@Composable
fun BillingInfoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier.padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ReceiptLong,
                    contentDescription = "Icono Facturación",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Información de Facturación",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            HorizontalDivider(modifier = Modifier.fillMaxWidth())

            Text(
                text = "Último pago: 10/10/2023",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = "Estado: Pagado",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun SubscriptionsList(
    subsModMap: StateFlow<Map<SubscriptionDTO, ModuleDTO>>,
    onAddClick: () -> Unit = {},
    onToggleAccess: (SubscriptionDTO) -> Unit = {}
) {
    val subsMap by subsModMap.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Suscripciones",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                IconButton(
                    onClick = onAddClick,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar suscripción",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            if (subsMap.isEmpty()) {
                Text(
                    text = "No hay suscripciones activas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            } else {
                subsMap.forEach { (subscription, module) ->
                    SubscriptionCard(subscription, module) { onToggleAccess(subscription) }
                }
            }
        }
    }
}

@Composable
fun SubscriptionCard(
    subscription: SubscriptionDTO,
    module: ModuleDTO,
    onToggleAccess: (SubscriptionDTO) -> Unit
) {
    var showConfirmDialog by remember { mutableStateOf(false) }


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Módulo: ${module.name}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (subscription.isActive) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = "Activo",
                        tint = if (subscription.isActive) Color(0xFF388E3C) else Color(0xFFD32F2F)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Activo: ${if (subscription.isActive) "Sí" else "No"}")
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (subscription.isAccessible) Icons.Default.CheckCircle else Icons.Default.Cancel,
                        contentDescription = "Accesible",
                        tint = if (subscription.isAccessible) Color(0xFF1976D2) else Color(0xFF757575)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Accesible: ${if (subscription.isAccessible) "Sí" else "No"}")
                }
            }

            IconButton(
                onClick = { showConfirmDialog = true },
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                    imageVector = if (subscription.isAccessible) Icons.Default.Block else Icons.Default.CheckCircle,
                    contentDescription = if (subscription.isAccessible) "Revocar acceso" else "Conceder acceso",
                    tint = if (subscription.isAccessible) Color(0xFFD32F2F) else Color(0xFF388E3C)
                )
            }
        }
    }

    if (showConfirmDialog) {
        val newState = !subscription.isAccessible
        AlertDialog(
            onDismissRequest = { showConfirmDialog = false },
            title = {
                Text(text = if (newState) "Conceder Acceso" else "Revocar Acceso")
            },
            text = {
                Text(
                    text = if (newState)
                        "¿Estás seguro que deseas conceder acceso al módulo?"
                    else
                        "¿Estás seguro que deseas revocar el acceso al módulo?"
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    showConfirmDialog = false
                    onToggleAccess(subscription)
                }) {
                    Text("Aceptar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showConfirmDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}




