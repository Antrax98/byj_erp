package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.nav.addRole

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.common.BusyOverlay

@Composable
fun AddRoleScreen(component: AddRoleComponent) {
    val name by component.name.collectAsState()
    val description by component.description.collectAsState()

    val isBusy by component.isBusy.collectAsState()
    BusyOverlay(isBusy)

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.TopCenter
    ) {
        Card(
            modifier = Modifier
                .padding(top = 32.dp)
                .fillMaxWidth(0.95f),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                horizontalAlignment = Alignment.Start
            ) {

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Nombre del rol", style = MaterialTheme.typography.labelMedium)
                    OutlinedTextField(
                        value = name.value,
                        onValueChange = { component.onNameChange(it) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Badge,
                                contentDescription = "Icono de nombre de rol"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isError = name.error != null,
                        supportingText = {
                            name.error?.let {
                                Text(it, color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )
                }

                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Descripción", style = MaterialTheme.typography.labelMedium)
                    OutlinedTextField(
                        value = description.value,
                        onValueChange = { component.onDescriptionChange(it) },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Icono de descripción"
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        isError = description.error != null,
                        supportingText = {
                            description.error?.let {
                                Text(it, color = MaterialTheme.colorScheme.error)
                            }
                        }
                    )
                }

                Button(
                    onClick = { component.onSubmitted() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Crear rol")
                }
            }
        }
    }
}
