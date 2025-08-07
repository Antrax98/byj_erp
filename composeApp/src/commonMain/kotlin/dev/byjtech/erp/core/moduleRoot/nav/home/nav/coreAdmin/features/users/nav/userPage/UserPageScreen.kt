package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.userPage

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
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.PermissionWithKey
import dev.byjtech.erp.common.tools.containsAnyOf
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.dto.RoleDTO
import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.UsersFeatureComponentImpl.Config
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toJavaLocalDateTime
import kotlinx.datetime.toKotlinLocalDateTime
import kotlinx.datetime.toLocalDateTime
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun UserPageScreen(component: UserPageComponent) {
    val userInfo by component.userInfo.collectAsState()
    val userSpecialPermissions by component.userSpecialPermissions.collectAsState()
    val userRoles by component.userRoles.collectAsState()
    val userPermissions by component.userPermissions.collectAsState()

    val userIsLoading by component.userIsLoading.collectAsState()
    val permissionsIsLoading by component.permissionsIsLoading.collectAsState()
    val rolesIsLoading by component.rolesIsLoading.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(16.dp)
    ) {
        item {
            if (userIsLoading || userInfo == null) {
                CircularProgressIndicator()
            } else {
                UserDetailsScreen(user = userInfo!!, component)
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Roles",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        IconButton(
                            onClick = {
                                component.navTo(
                                    Config.AssignRole(
                                        userId = component.userId,
                                        actUserRoles = userRoles?.toSet() ?: emptySet()
                                    )
                                )
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Agregar rol",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    when {
                        rolesIsLoading -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Cargando roles...")
                            }
                        }

                        userRoles != null && userRoles!!.isEmpty() -> {
                            Text(
                                text = "El usuario no tiene roles asignados.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }

                        userRoles != null -> {
                            val canUnassignRole =
                                userPermissions.containsAnyOf(CoreDefinition.Roles.Unassign.key)
                            userRoles!!.forEach { role ->
                                RoleCard(role, canUnassignRole) {
                                    component.deleteRole(role.id)
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Permisos especiales",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        if (userPermissions.containsAnyOf(CoreDefinition.Roles.Assign.key)) {
                            IconButton(
                                onClick = {
                                    component.navTo(
                                        Config.AssignSpecialPermission(userId = component.userId)
                                    )
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Agregar permiso",
                                    tint = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    when {
                        permissionsIsLoading -> {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 16.dp),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp))
                                Spacer(modifier = Modifier.width(12.dp))
                                Text("Cargando permisos especiales...")
                            }
                        }

                        userSpecialPermissions != null && userSpecialPermissions!!.isEmpty() -> {
                            Text(
                                text = "El usuario no tiene permisos especiales asignados.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        }

                        userSpecialPermissions != null -> {
                            val canUnassignPermission =
                                userPermissions.containsAnyOf(CoreDefinition.Roles.Unassign.key)
                            userSpecialPermissions!!.forEach { perm ->
                                PermissionKeyCard(
                                    perm,
                                    canUnassignPermission
                                ) { component.deleteSpecialPermission(perm.permission.id) }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun PermissionKeyCard(permissionWithKey: PermissionWithKey, canUnassignPermission: Boolean, onActionClick: () -> Unit = {}) {
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
                    text = "Módulo: ${permissionWithKey.key.module}",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Categoría: ${permissionWithKey.key.category}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Acción: ${permissionWithKey.key.action}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }
            if (canUnassignPermission) {
                IconButton(onClick = { showDialog=true }, enabled = canUnassignPermission) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Unassign Special Permission"
                    )
                }
            }

        }
    }
    if (showDialog){
        AlertDialog(
            onDismissRequest = { showDialog=false },
            title = { Text(text = "Eliminar permiso especial") },
            text = { Text(text = "¿Estás seguro de que deseas eliminar este permiso especial?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onActionClick()
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
fun UserDetailsScreen(user: UserDTO, component: UserPageComponent) {
    val showEditNameDialog = remember { mutableStateOf(false) }
    val nameInput = remember { mutableStateOf(user.name ?: "") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Card(
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth()
            ) {
                if (!user.pictureUrl.isNullOrBlank()) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "User Icon",
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                            .padding(20.dp)
                            .align(Alignment.CenterHorizontally),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = user.name ?: "Sin nombre",
                        style = MaterialTheme.typography.titleLarge
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(onClick = { showEditNameDialog.value = true }) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Editar nombre",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(24.dp))

                InfoRow("Activo", if (user.isActive) "Sí" else "No")

                user.createdAt?.let {
                    InfoRowDate("Creado el", it.toJavaLocalDateTime())
                }

                InfoRowDate(
                    "Actualizado el",
                    user.updatedAt?.toJavaLocalDateTime()
                        ?: Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).toJavaLocalDateTime()
                )
            }
        }
    }

    if (showEditNameDialog.value) {
        AlertDialog(
            onDismissRequest = { showEditNameDialog.value = false },
            title = { Text("Editar nombre del usuario") },
            text = {
                TextField(
                    value = nameInput.value,
                    onValueChange = { nameInput.value = it },
                    placeholder = { Text("Nombre del usuario") },
                    singleLine = true
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    component.updateUserName(nameInput.value)
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
fun InfoRow(label: String, value: String) {
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
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}



@Composable
fun RoleCard(role: RoleDTO, canUnassignRole: Boolean, onActionClick: () -> Unit = {}) {
    var showDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = "Role Icon",
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 16.dp),
                tint = Color(0xFF4CAF50)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = role.name,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = role.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            }

            if (canUnassignRole) {
                IconButton(onClick = {showDialog = true}) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Delete user role"
                    )
                }
            }
        }
    }
    if (showDialog){
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "Eliminar rol del usuario") },
            text = { Text(text = "¿Estás seguro de que deseas eliminar este rol?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    onActionClick()
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



