package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.rolesMain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.common.UnderConstructionScreen
import dev.byjtech.erp.common.tools.containsAnyOf
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.dto.RoleDTO
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

@Composable
fun RolesMainScreen(component: RolesMainComponent){
    val isLoading by component.isLoading.collectAsState()
    val rolesSet by component.rolesState.collectAsState()
    val userPermissions by component.userPermissions.collectAsState()

    Scaffold(
        floatingActionButton = {
            if(userPermissions.containsAnyOf(setOf(CoreDefinition.Users.Create.key))){
                FloatingActionButton(
                    onClick = { /*component.navTo(Config.AddRole)*/println("Clicked on add Role") },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar Rol")
                }
            }
        }
    ) {
        if(isLoading){
            UnderConstructionScreen("Cargando roles...")
            CircularProgressIndicator()
        }else{
            if(rolesSet != null){
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top
                ) {
                    rolesSet?.forEach { role ->
                        item {
                            RoleContainer(role, onClick = {println("Clicked on role ${role.name}")})
                        }
                    }
                    if(rolesSet!!.isEmpty()){
                        item {
                            Text(text = "No hay roles en la empresa")
                        }
                        testRoles.forEach { role ->
                            item {
                                RoleContainer(role, onClick = {println("Clicked on role ${role.name}")})
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.size(100.dp))

                    }
                }
            } else {
                UnderConstructionScreen("No hay roles cargados")
            }
        }
    }


}

@Composable
fun RoleContainer(role: RoleDTO, onClick: () -> Unit = {}){
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row {
            Icon(
                imageVector = Icons.Default.Task,
                contentDescription = "Role Icon",
                modifier = Modifier
                    .size(75.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(10.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Text(
                text = role.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = onClick,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                    contentDescription = "To Role info",
                )
            }
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
