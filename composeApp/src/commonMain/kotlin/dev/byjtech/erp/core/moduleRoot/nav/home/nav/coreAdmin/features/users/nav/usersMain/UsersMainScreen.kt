package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.usersMain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.common.getPlatform
import dev.byjtech.erp.common.tools.containsAnyOf
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.UsersFeatureComponentImpl

@Composable
fun UsersMainScreen(component: UsersMainComponent) {

    val state by component.state.collectAsState()
    val usersList by component.usersList.collectAsState()
    val userPermissions by component.userPermissions.collectAsState()

    Scaffold (
        floatingActionButton = {
            if(userPermissions.containsAnyOf(setOf(CoreDefinition.Users.Create.key))){
                FloatingActionButton(
                    onClick = { component.navTo(UsersFeatureComponentImpl.Config.AddUser) },
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar usuario")
                }
            }
        }

    ){
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text("Users Main Screen")
            Text("actual platform ${getPlatform()}")

            if(state.isLoading){
                CircularProgressIndicator()

            }else{
                if(usersList != null){
                    LazyColumn {
                        usersList?.let { list ->
                            items(list) { user ->
                                UserContainer(user, onClick = {component.navTo(UsersFeatureComponentImpl.Config.UserPage(user.id))})
                            }
                        }
                        fakeUsers.forEach { user ->
                            item {
                                UserContainer(user)
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

//TODO: hacerlo mas generico para poder usarlo en todas las demas listas
@Composable
fun UserContainer(user: UserDTO, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Person Icon",
                modifier = Modifier
                    .size(75.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
                    .padding(10.dp),
                tint = MaterialTheme.colorScheme.primary,
            )
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = user.name ?: "Sin nombre",
                    style = MaterialTheme.typography.titleMedium
                )
                user.email.let {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.Gray
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = onClick,
                modifier = Modifier.align(Alignment.CenterVertically)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowRight,
                    contentDescription = "To user info",
                )
            }
        }
    }
}

//para pblar un poco la lista
private val fakeUsers = listOf(
    UserDTO(1, "Juan Pérez", "juan@example.com", "google-1", null, true, null, null, 101),
    UserDTO(2, "María Gómez", "maria@example.com", "google-2", null, true, null, null, 101),
    UserDTO(3, "Carlos Ruiz", "carlos@example.com", "google-3", null, false, null, null, 102),
    UserDTO(4, "Ana Torres", "ana@example.com", "google-4", null, true, null, null, 103),
    UserDTO(5, "Luis Martínez", "luis@example.com", "google-5", null, true, null, null, 104),
    UserDTO(6, "Laura Fernández", "laura@example.com", "google-6", null, false, null, null, 105),
    UserDTO(7, "Pedro Jiménez", "pedro@example.com", "google-7", null, true, null, null, 101),
    UserDTO(8, "Sofía Herrera", "sofia@example.com", "google-8", null, true, null, null, 102),
    UserDTO(9, "Diego Castro", "diego@example.com", "google-9", null, false, null, null, 103),
    UserDTO(10, "Lucía Rojas", "lucia@example.com", "google-10", null, true, null, null, 104)
)