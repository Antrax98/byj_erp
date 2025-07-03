package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.addPermission

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.common.PermissionKey

@Composable
fun AddPermissionScreen(component: AddPermissionComponent) {
    val possiblePermissions by component.possiblePermissions.collectAsState()
    val isLoading by component.isLoading.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(4.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                for ((module, permissions) in possiblePermissions) {
                    Text(
                        modifier = Modifier.padding(16.dp).align(Alignment.CenterHorizontally),
                        text = module,
                        style = MaterialTheme.typography.titleLarge
                    )
                    for (permission in permissions) {
                        Row(
                            modifier = Modifier.padding(2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            PermissionCard(permission.key, onClick = {component.addPermission(permission.key)})
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PermissionCard(key: PermissionKey, onClick: () -> Unit) {
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
            Column {
                Row {
                    Text("Categoria: ${key.category}")
                }
                Row {
                    Text("Acción: ${key.action}")
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(
                onClick = { onClick() }
            ){
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Permission"
                )
            }
        }

    }
}