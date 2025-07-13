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
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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

@Composable
fun UserPageScreen(component: UserPageComponent) {

    val userInfo by component.userInfo.collectAsState()
    val userSpecialPermissions by component.userSpecialPermissions.collectAsState()
    val userRoles by component.userRoles.collectAsState()

    val userPermissions by component.userPermissions.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(16.dp)
    ) {
        // info del usuario
        item {
            if (userInfo != null) {
                UserDetailsScreen(user = userInfo!!)
            } else {
                CircularProgressIndicator()
            }
        }

        //roles en su card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ){
                        Text(
                            text = "Roles",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Button(
                            onClick = { component.navTo(Config.AssignRole(userId = component.userId, actUserRoles = userRoles?.toSet()?: emptySet())) }
                        ){
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add role to user",
                                modifier = Modifier.size(20.dp),
                                tint = Color.White
                            )
                        }

                    }

                    if (userRoles != null) {
                        if (userRoles!!.isEmpty()) {
                            Text(
                                text = "El usuario no tiene roles asignados.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        } else {
                            val canUnassignRole = userPermissions.containsAnyOf(CoreDefinition.Roles.Unassign.key)
                            userRoles!!.forEach { role ->
                                RoleCard(role, canUnassignRole){
                                    component.deleteRole(role.id)
                                }
                            }
                        }
                    } else {
                        CircularProgressIndicator()
                    }
                }
            }
        }

        //permisos especiales en un card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row (
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically

                    ){
                        Text(
                            text = "Permisos especiales",
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        if (userPermissions.containsAnyOf(CoreDefinition.Roles.Assign.key)) {
                            Button(
                                onClick = { component.navTo(Config.AssignSpecialPermission(userId = component.userId)) }
                            ){
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add special permission to user",
                                    modifier = Modifier.size(20.dp),
                                    tint = Color.White
                                )
                            }
                        }
                    }


                    if (userSpecialPermissions != null) {
                        if (userSpecialPermissions!!.isEmpty()) {
                            Text(
                                text = "El usuario no tiene permisos especiales asignados.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )
                        } else {
                            val canUnassignPermission = userPermissions.containsAnyOf(CoreDefinition.Roles.Unassign.key)
                            userSpecialPermissions!!.forEach { perm ->
                                PermissionKeyCard(perm, canUnassignPermission,{component.deleteSpecialPermission(perm.permission.id)})
                            }
                        }
                    } else {
                        CircularProgressIndicator()
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
fun UserDetailsScreen(user: UserDTO) {
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
                // foto de perfil en un futuro
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

                Text(
                    text = user.name ?: "Sin nombre",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Text(
                    text = user.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Info adicional
                InfoRow("Google ID", user.googleId ?: "No vinculado")
                InfoRow("Activo", if (user.isActive) "Sí" else "No")

                user.createdAt?.let {
                    InfoRow("Creado el", it.toString())
                }

                user.updatedAt?.let {
                    InfoRow("Actualizado el", it.toString())
                }
            }
        }
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


