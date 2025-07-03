package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolesMain

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Task
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.common.UnderConstructionScreen
import dev.byjtech.erp.common.tools.containsAnyOf
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.dto.RoleDTO
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.RolesFeatureComponentImpl.Config
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

@Composable
fun RolesMainScreen(component: RolesMainComponent) {
    val isLoading by component.isLoading.collectAsState()
    val rolesSet by component.rolesState.collectAsState()
    val userPermissions by component.userPermissions.collectAsState()

    Scaffold(
        floatingActionButton = {
            if (userPermissions.containsAnyOf(setOf(CoreDefinition.Users.Create.key))) {
                FloatingActionButton(
                    onClick = { component.navTo(Config.AddRole) },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Rol")
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when {
                isLoading -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Cargando roles...")
                    }
                }

                rolesSet == null -> {
                    UnderConstructionScreen("No se pudieron cargar los roles")
                }

                rolesSet!!.isEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(top = 32.dp)
                    ) {
                        item {
                            Text(
                                text = "No hay roles en la empresa",
                                style = MaterialTheme.typography.bodyMedium,
                                //modifier = Modifier.align(Alignment.CenterHorizontally)
                            )
                        }
//                        testRoles.forEach { role ->
//                            item {
//                                RoleContainer(role) {
//                                    println("Clicked on test role ${role.name}")
//                                }
//                            }
//                        }
                        item {
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        rolesSet!!.forEach { role ->
                            item {
                                RoleContainer(role) {
                                    component.navTo(Config.RolePage(role.id))
                                }
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RoleContainer(role: RoleDTO, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Row(
            modifier = Modifier
                .clickable { onClick() }
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Task,
                    contentDescription = "Role Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = role.name,
                    style = MaterialTheme.typography.titleMedium
                )
                if (role.description.isNotBlank()) {
                    Text(
                        text = role.description,
                        style = MaterialTheme.typography.bodySmall,
                        color = Color.Gray,
                        maxLines = 1
                    )
                }
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                contentDescription = "Ver más",
                tint = Color.Gray
            )
        }
    }
}


val testRoles = setOf(
    RoleDTO(
        id = "c7e9c57c-4e2e-4a3a-bf12-dce2ef28d70a",
        name = "Admin",
        description = "Full access to the system",
        createdAt = LocalDateTime.now().toKotlinLocalDateTime(),
        updatedAt = LocalDateTime.now().minusDays(1).toKotlinLocalDateTime(),
        companyId = "8e4294b4-bc2a-457d-8758-e7f14a9462a5"
    ),
    RoleDTO(
        id = "1a4d4ac6-c7f0-49d4-bf6f-b5d81267a903",
        name = "User",
        description = "Standard user role",
        createdAt = LocalDateTime.now().minusDays(15).toKotlinLocalDateTime(),
        updatedAt = LocalDateTime.now().minusDays(2).toKotlinLocalDateTime(),
        companyId = "8e4294b4-bc2a-457d-8758-e7f14a9462a5"
    ),
    RoleDTO(
        id = "72c6cf2a-162e-463b-84a5-4b0c4a7b861f",
        name = "Viewer",
        description = "Read-only access",
        createdAt = LocalDateTime.now().minusDays(20).toKotlinLocalDateTime(),
        updatedAt = null,
        companyId = "8e4294b4-bc2a-457d-8758-e7f14a9462a5"
    ),
    RoleDTO(
        id = "9c438f89-b2c3-4e7f-9d3c-53b94e7db44b",
        name = "Manager",
        description = "Manages teams and permissions",
        createdAt = LocalDateTime.now().minusDays(5).toKotlinLocalDateTime(),
        updatedAt = LocalDateTime.now().minusDays(1).toKotlinLocalDateTime(),
        companyId = "17cf6460-6167-4eb0-a4f5-017d68c73de3"
    ),
    RoleDTO(
        id = "0efbeec2-30b2-4ad3-917b-0bc67618a08a",
        name = "Support",
        description = "Helps users with technical issues",
        createdAt = LocalDateTime.now().minusDays(30).toKotlinLocalDateTime(),
        updatedAt = null,
        companyId = "17cf6460-6167-4eb0-a4f5-017d68c73de3"
    ),
    RoleDTO(
        id = "3832f2db-6dd0-46b4-8780-3d1c80dfb720",
        name = "Auditor",
        description = "Can audit activity logs",
        createdAt = LocalDateTime.now().minusDays(7).toKotlinLocalDateTime(),
        updatedAt = LocalDateTime.now().minusDays(3).toKotlinLocalDateTime(),
        companyId = "2e94c03f-b2b5-4454-b964-dae4db78299b"
    )
)
