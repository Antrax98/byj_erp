package dev.byjtech.erp.modules.machinery.create

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MachineryCreateScreen(component: MachineryCreateComponent) {
    val state by component.state.collectAsState()
    val code by component.code.collectAsState()
    val name by component.name.collectAsState()
    val description by component.description.collectAsState()
    val brand by component.brand.collectAsState()
    val model by component.model.collectAsState()
    val year by component.year.collectAsState()
    val serialNumber by component.serialNumber.collectAsState()
    val licensePlate by component.licensePlate.collectAsState()
    val location by component.location.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Header
        Text(
            text = "Crear Nueva Maquinaria",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )

        // Error message
        if (state.error != null) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = state.error!!,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Form fields
        OutlinedTextField(
            value = code,
            onValueChange = component::onCodeChange,
            label = { Text("Código *") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            singleLine = true
        )

        OutlinedTextField(
            value = name,
            onValueChange = component::onNameChange,
            label = { Text("Nombre *") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            singleLine = true
        )

        OutlinedTextField(
            value = description,
            onValueChange = component::onDescriptionChange,
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
                value = brand,
                onValueChange = component::onBrandChange,
                label = { Text("Marca *") },
                modifier = Modifier.weight(1f),
                enabled = !state.isLoading,
                singleLine = true
            )

            OutlinedTextField(
                value = model,
                onValueChange = component::onModelChange,
                label = { Text("Modelo *") },
                modifier = Modifier.weight(1f),
                enabled = !state.isLoading,
                singleLine = true
            )
        }

        OutlinedTextField(
            value = year,
            onValueChange = component::onYearChange,
            label = { Text("Año") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        OutlinedTextField(
            value = serialNumber,
            onValueChange = component::onSerialNumberChange,
            label = { Text("Número de Serie") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            singleLine = true
        )

        OutlinedTextField(
            value = licensePlate,
            onValueChange = component::onLicensePlateChange,
            label = { Text("Placa") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            singleLine = true
        )

        OutlinedTextField(
            value = location,
            onValueChange = component::onLocationChange,
            label = { Text("Ubicación") },
            modifier = Modifier.fillMaxWidth(),
            enabled = !state.isLoading,
            singleLine = true
        )

        // Action buttons
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = component::onCancel,
                modifier = Modifier.weight(1f),
                enabled = !state.isLoading
            ) {
                Text("Cancelar")
            }

            Button(
                onClick = component::onSave,
                modifier = Modifier.weight(1f),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(16.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Icon(
                        Icons.Filled.Save,
                        contentDescription = "Guardar",
                        modifier = Modifier.size(18.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text("Guardar")
            }
        }

        // Required fields note
        Text(
            text = "* Campos requeridos",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
