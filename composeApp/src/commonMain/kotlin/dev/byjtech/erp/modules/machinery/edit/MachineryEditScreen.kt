package dev.byjtech.erp.modules.machinery.edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachineryEditScreen(
    component: MachineryEditComponent,
    modifier: Modifier = Modifier
) {
    val state by component.state.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    // Manejar mensajes con Snackbar
    LaunchedEffect(state.successMessage, state.errorMessage) {
        when {
            state.successMessage != null -> {
                snackbarHostState.showSnackbar(
                    message = "✅ ${state.successMessage}",
                    duration = SnackbarDuration.Long
                )
                component.clearMessages()
            }
            state.errorMessage != null -> {
                snackbarHostState.showSnackbar(
                    message = "❌ ${state.errorMessage}",
                    actionLabel = "Cerrar",
                    duration = SnackbarDuration.Long
                )
            }
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text("Editar Maquinaria") },
                navigationIcon = {
                    IconButton(onClick = { component.goBack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Información de la Maquinaria",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            // Campos del formulario
            OutlinedTextField(
                value = state.name,
                onValueChange = component::updateName,
                label = { Text("Nombre *") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading,
                isError = state.name.isBlank()
            )

            OutlinedTextField(
                value = state.description,
                onValueChange = component::updateDescription,
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading,
                maxLines = 3
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.brand,
                    onValueChange = component::updateBrand,
                    label = { Text("Marca *") },
                    modifier = Modifier.weight(1f),
                    enabled = !state.isLoading,
                    isError = state.brand.isBlank()
                )

                OutlinedTextField(
                    value = state.model,
                    onValueChange = component::updateModel,
                    label = { Text("Modelo *") },
                    modifier = Modifier.weight(1f),
                    enabled = !state.isLoading,
                    isError = state.model.isBlank()
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = state.year,
                    onValueChange = component::updateYear,
                    label = { Text("Año") },
                    modifier = Modifier.weight(1f),
                    enabled = !state.isLoading
                )

                OutlinedTextField(
                    value = state.serialNumber,
                    onValueChange = component::updateSerialNumber,
                    label = { Text("Número de Serie") },
                    modifier = Modifier.weight(1f),
                    enabled = !state.isLoading
                )
            }

            OutlinedTextField(
                value = state.licensePlate,
                onValueChange = component::updateLicensePlate,
                label = { Text("Placa") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            )

            OutlinedTextField(
                value = state.location,
                onValueChange = component::updateLocation,
                label = { Text("Ubicación") },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading
            )

            // Estado dropdown
            var statusExpanded by remember { mutableStateOf(false) }
            val statusOptions = listOf(
                "ACTIVE" to "Activo",
                "MAINTENANCE" to "En Mantenimiento", 
                "INACTIVE" to "Inactivo"
            )

            ExposedDropdownMenuBox(
                expanded = statusExpanded,
                onExpandedChange = { statusExpanded = !statusExpanded },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = statusOptions.find { it.first == state.status }?.second ?: state.status,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Estado") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = statusExpanded) },
                    colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    enabled = !state.isLoading
                )
                ExposedDropdownMenu(
                    expanded = statusExpanded,
                    onDismissRequest = { statusExpanded = false }
                ) {
                    statusOptions.forEach { (value, label) ->
                        DropdownMenuItem(
                            text = { Text(label) },
                            onClick = {
                                component.updateStatus(value)
                                statusExpanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de guardar
            Button(
                onClick = { component.saveMachinery() },
                modifier = Modifier.fillMaxWidth(),
                enabled = !state.isLoading && state.name.isNotBlank() && state.brand.isNotBlank() && state.model.isNotBlank()
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
                Text("Guardar Cambios")
            }
        }
    }
}
