package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolePage

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import dev.byjtech.erp.common.BusyOverlay
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.UnderConstructionScreen
import dev.byjtech.erp.common.tools.containsAnyOf
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.dto.RoleDTO
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.RolesFeatureComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.userPage.InfoRow
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun RolePageScreen(component: RolePageComponent) {
    val role by component.roleInfo.collectAsState()
    val permissions by component.rolePermissions.collectAsState()
    val isLoading by component.isLoading.collectAsState()
    val isPermissionLoading by component.isPermissionLoading.collectAsState()
    val isBusy by component.isBusy.collectAsState()
    val userPermissions by component.userPermissions.collectAsState()

    BusyOverlay(isBusy)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(16.dp)
    ) {
        // Card: Información del Rol
        item {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                role?.let {
                    RoleInfoCard(role = it, component)
                } ?: Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(150.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No se encontró información del rol.", color = Color.Gray)
                }
            }
        }

        // Card: Permisos asignados al Rol
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Permisos del Rol",
                            style = MaterialTheme.typography.titleMedium
                        )
                        if (userPermissions.containsAnyOf(setOf(CoreDefinition.Roles.Update.key))) {
                            IconButton(onClick = {
                                component.navTo(RolesFeatureComponentImpl.Config.AddPermission(component.roleId))
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Agregar permiso"
                                )
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    if (isPermissionLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    } else {
                        if (permissions.isEmpty()) {
                            Text(
                                "Este rol no tiene permisos asignados.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        } else {
                            permissions.forEach { permission ->
                                PermissionKeyCard(
                                    permissionKey = permission.key,
                                    canUnassignPermission = true
                                ) {
                                    component.deletePermission(permission.permission.id)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun RoleInfoCard(role: RoleDTO, component: RolePageComponent) {
    // Estados para el diálogo
    val showEditNameDialog = remember { mutableStateOf(false) }
    val nameInput = remember { mutableStateOf(role.name) }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(
                    text = role.name,
                    style = MaterialTheme.typography.titleLarge,
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = { showEditNameDialog.value = true }) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "Editar nombre del rol",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Text(
                text = role.description,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))

            InfoRowDate(
                "Creado el",
                role.createdAt?.toJavaLocalDateTime()
                    ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
            )

            InfoRowDate(
                "Actualizado el",
                role.updatedAt?.toJavaLocalDateTime()
                    ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
            )
        }
    }

    // Diálogo para editar el nombre
    if (showEditNameDialog.value) {
        AlertDialog(
            onDismissRequest = { showEditNameDialog.value = false },
            title = { Text("Editar nombre del rol") },
            text = {
                TextField(
                    value = nameInput.value,
                    onValueChange = { nameInput.value = it },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    component.updateRoleName(nameInput.value)
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
}



@Composable
fun PermissionKeyCard(
    permissionKey: PermissionKey,
    canUnassignPermission: Boolean,
    onDeleteClick: () -> Unit = {}
) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 6.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Lock,
                contentDescription = "Permission Icon",
                modifier = Modifier
                    .size(30.dp)
                    .padding(end = 12.dp),
                tint = Color(0xFF1976D2)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Módulo: ${permissionKey.module}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Categoría: ${permissionKey.category}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Text(
                    text = "Acción: ${permissionKey.action}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            if (canUnassignPermission) {
                IconButton(onClick = { showDialog = true }) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar Permiso",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Eliminar permiso") },
            text = {
                Text("¿Estás seguro que quieres eliminar el permiso '${permissionKey.action}' del módulo '${permissionKey.module}'?")
            },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onDeleteClick()
                }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }
}


@Composable
fun PermissionCard(
    permission: PermissionKey,
    onClick: () -> Unit
) {

    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Categoría:",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.width(100.dp)
                    )
                    Text(
                        text = permission.category.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Acción:",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.width(100.dp)
                    )
                    Text(
                        text = permission.action.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }

            IconButton(
                onClick = { showDialog = true },
                modifier = Modifier.size(36.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Quitar permiso",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }
    }
    if(showDialog){
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Eliminar permiso") },
            text = { Column {
                Text("¿Estás seguro de que quieres eliminar este permiso del rol?")
                Text("Modulo: ${permission.module}")
                Text("Categoría: ${permission.category}")
                Text("Acción: ${permission.action}")
            } },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDialog = false
                        onClick()
                    }
                ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { showDialog = false }
                ) {
                    Text("Cancelar")
                }
            }
        )
    }
}



@Composable
fun InfoRowDate(label: String, dateTime: LocalDateTime) {
    val formatter = DateTimeFormatter.ofPattern("d 'de' MMMM 'de' yyyy, HH:mm", Locale("es", "ES"))
    val formattedDate = dateTime.format(formatter)


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Info,
            contentDescription = null,
            modifier = Modifier.size(20.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "$label: ",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = formattedDate,
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}
