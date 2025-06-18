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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.common.getPlatform
import dev.byjtech.erp.common.tools.containsAnyOf
import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.UsersFeatureComponentImpl.Config

@Composable
fun UsersMainScreen(component: UsersMainComponent) {

    val state by component.state.collectAsState()
    val usersList by component.usersList.collectAsState()
    val userPermissions by component.userPermissions.collectAsState()

    Scaffold (
        floatingActionButton = {
            if(userPermissions.containsAnyOf(setOf(CoreDefinition.Users.Create.key))){
                FloatingActionButton(
                    onClick = { component.navTo(Config.AddUser) },
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
                                UserContainer(user, onClick = {component.navTo(Config.UserPage(user.id))})
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
    UserDTO("e8f5d7c3-4c2e-4b7e-a989-1d00de1ef7c1", "Juan Pérez", "juan@example.com", "google-1", null, true, null, null, "6a6408cd-13ec-4c9c-90f3-f8f1567feeb8"),
    UserDTO("7cbbce34-82fa-4ae6-bf23-362f0556b0b9", "María Gómez", "maria@example.com", "google-2", null, true, null, null, "ba7d30a2-8c2f-4028-9c3a-75a72dc09c7e"),
    UserDTO("a32b6474-113b-4d23-a5f2-6f9bde7d989e", "Carlos Ruiz", "carlos@example.com", "google-3", null, false, null, null, "9f64ec42-80a3-41ee-8a0a-08f49d29e202"),
    UserDTO("3d0bfc94-0085-4cb4-9d6d-8e2f2c6bdf8f", "Ana Torres", "ana@example.com", "google-4", null, true, null, null, "d65a4a89-e94f-4cc8-95b3-e1a7c8bcddc3"),
    UserDTO("58e1bc0f-e8e6-4db8-8fae-2011793a5c26", "Luis Martínez", "luis@example.com", "google-5", null, true, null, null, "cb9476a5-bc89-423a-9ff4-5f59403a24e9"),
    UserDTO("413ebbed-1d44-4e6f-a70f-40341a143fd9", "Laura Fernández", "laura@example.com", "google-6", null, false, null, null, "0d4a4cd4-ff11-4431-9eaf-f25d3429eefd"),
    UserDTO("4cc3c0b7-9916-4d40-8f3c-6b8c6d94f35c", "Pedro Jiménez", "pedro@example.com", "google-7", null, true, null, null, "adddb77b-3fd6-4375-8233-63f447de308b"),
    UserDTO("0e2b8758-525a-497d-9864-17d9d10e3f71", "Sofía Herrera", "sofia@example.com", "google-8", null, true, null, null, "27ef630f-95cb-417e-97e0-19751a02a84d"),
    UserDTO("8b2ad8cb-882f-4e64-99a7-0a5c0e4e5c28", "Diego Castro", "diego@example.com", "google-9", null, false, null, null, "e52b4a96-4f7a-439a-9238-f59a62dc1320"),
    UserDTO("fcd799e2-49c0-4968-a674-ecbe68fe3056", "Lucía Rojas", "lucia@example.com", "google-10", null, true, null, null, "b71f6485-8e9b-4ed9-b2be-6b1bcba6936f")
)
