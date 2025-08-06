package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.addPermission

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.common.BusyOverlay
import dev.byjtech.erp.common.PermissionWithKey

@Composable
fun AddPermissionScreen(component: AddPermissionComponent) {
    val possiblePermissions by component.possiblePermissions.collectAsState()
    val isLoading by component.isLoading.collectAsState()
    val isBusy by component.isBusy.collectAsState()

    val expandedModules = remember { mutableStateMapOf<String, Boolean>() }
    val expandedCategories = remember { mutableStateMapOf<Pair<String, String>, Boolean>() }

    BusyOverlay(isBusy)

    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                CircularProgressIndicator()
                Spacer(Modifier.height(8.dp))
                Text("Cargando permisos...", style = MaterialTheme.typography.bodyMedium)
            }
        }
        return
    }

    LaunchedEffect(possiblePermissions) {
        for ((module, permissions) in possiblePermissions) {
            expandedModules.putIfAbsent(module, false)
            val groupedByCategory = permissions.groupBy { it.key.category }
            for (category in groupedByCategory.keys) {
                expandedCategories.putIfAbsent(module to category, false)
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        contentPadding = PaddingValues(8.dp)
    ) {
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    for ((module, permissionsSet) in possiblePermissions) {
                        val isModuleExpanded = expandedModules[module] == true
                        val permissions = permissionsSet.toList()

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { expandedModules[module] = !isModuleExpanded }
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = module,
                                style = MaterialTheme.typography.titleLarge,
                                modifier = Modifier.weight(1f)
                            )
                            Icon(
                                imageVector = if (isModuleExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null
                            )
                        }

                        if (isModuleExpanded) {
                            val groupedByCategory = permissions.groupBy { it.key.category }

                            for ((category, permissionList) in groupedByCategory) {
                                val categoryKey = module to category
                                val isCategoryExpanded = expandedCategories[categoryKey] == true

                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            expandedCategories[categoryKey] = !isCategoryExpanded
                                        }
                                        .padding(start = 24.dp, top = 4.dp, bottom = 4.dp, end = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = category,
                                        style = MaterialTheme.typography.titleMedium,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Icon(
                                        imageVector = if (isCategoryExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                        contentDescription = null
                                    )
                                }

                                if (isCategoryExpanded) {
                                    permissionList.forEach { permissionWithKey ->
                                        PermissionCard(
                                            permission = permissionWithKey,
                                            onClick = {
                                                component.addPermission(permissionWithKey.key)
                                            }
                                        )
                                    }
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
fun PermissionCard(permission: PermissionWithKey, onClick: () -> Unit) {
    val isAssigned = permission.permission.id.isBlank()

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
                Text("Categoría: ${permission.key.category}")
                Text("Acción: ${permission.key.action}")
            }

            Spacer(modifier = Modifier.weight(1f))

            if (isAssigned) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Ya asignado",
                    tint = Color.Green
                )
            } else {
                IconButton(onClick = onClick) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Agregar permiso"
                    )
                }
            }
        }
    }
}
