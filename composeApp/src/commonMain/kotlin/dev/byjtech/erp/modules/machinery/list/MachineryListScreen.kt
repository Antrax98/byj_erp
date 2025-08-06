package dev.byjtech.erp.modules.machinery.list

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PowerOff
import androidx.compose.material.icons.filled.PowerSettingsNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.byjtech.erp.modules.machinery.dto.MachineryDTO

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachineryListScreen(
    component: MachineryListComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.collectAsState()
    val machineryList by component.machineryList.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Manejar mensajes con Snackbar
    LaunchedEffect(state.successMessage, state.error) {
        when {
            state.successMessage != null -> {
                snackbarHostState.showSnackbar(
                    message = "✅ ${state.successMessage}",
                    duration = SnackbarDuration.Long
                )
                component.clearMessages()
            }
            state.error != null -> {
                snackbarHostState.showSnackbar(
                    message = "❌ ${state.error}",
                    actionLabel = "Cerrar",
                    duration = SnackbarDuration.Long
                )
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Lista de Maquinaria") },
                actions = {
                    IconButton(onClick = { component.refresh() }) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Actualizar"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                
                state.error != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: ${state.error}",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { component.refresh() }) {
                            Text("Reintentar")
                        }
                    }
                }
                
                machineryList.isEmpty() -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "No hay maquinaria disponible",
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { component.refresh() }) {
                            Text("Actualizar")
                        }
                    }
                }
                
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(machineryList.size) { index ->
                            val machinery = machineryList[index]
                            MachineryCard(
                                machinery = machinery,
                                onEdit = { component.onEditMachinery(machinery) },
                                onDeactivate = { component.onDeactivateMachinery(machinery) },
                                onActivate = { component.onActivateMachinery(machinery) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MachineryCard(
    machinery: MachineryDTO,
    onEdit: () -> Unit,
    onDeactivate: () -> Unit,
    onActivate: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showMenu by remember { mutableStateOf(false) }

    Card(
        modifier = modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header con nombre y botón de opciones
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = machinery.name,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                
                Box {
                    IconButton(onClick = { showMenu = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Opciones"
                        )
                    }
                    
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Editar") },
                            onClick = {
                                showMenu = false
                                onEdit()
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Edit,
                                    contentDescription = null
                                )
                            }
                        )
                        
                        if (machinery.isActive) {
                            DropdownMenuItem(
                                text = { Text("Desactivar") },
                                onClick = {
                                    showMenu = false
                                    onDeactivate()
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.PowerOff,
                                        contentDescription = null
                                    )
                                }
                            )
                        } else {
                            DropdownMenuItem(
                                text = { Text("Activar") },
                                onClick = {
                                    showMenu = false
                                    onActivate()
                                },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.PowerSettingsNew,
                                        contentDescription = null
                                    )
                                }
                            )
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(4.dp))
            
            if (!machinery.description.isNullOrBlank()) {
                Text(
                    text = machinery.description ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (!machinery.manufacturer.isNullOrBlank()) {
                    Column {
                        Text(
                            text = "Fabricante",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = machinery.manufacturer ?: "",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                
                if (!machinery.model.isNullOrBlank()) {
                    Column {
                        Text(
                            text = "Modelo",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = machinery.model ?: "",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
                
                Column {
                    Text(
                        text = "Estado",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = when (machinery.status) {
                            "ACTIVE" -> "Activo"
                            "MAINTENANCE" -> "En Mantenimiento"
                            "INACTIVE" -> "Inactivo"
                            else -> machinery.status
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = when {
                            !machinery.isActive || machinery.status == "INACTIVE" -> MaterialTheme.colorScheme.error
                            machinery.status == "ACTIVE" -> MaterialTheme.colorScheme.primary
                            machinery.status == "MAINTENANCE" -> MaterialTheme.colorScheme.secondary
                            else -> MaterialTheme.colorScheme.onSurface
                        }
                    )
                }
            }
        }
    }
}
