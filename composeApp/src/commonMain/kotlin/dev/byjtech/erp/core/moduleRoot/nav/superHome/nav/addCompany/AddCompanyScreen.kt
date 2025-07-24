package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addCompany

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.common.BusyOverlay
import dev.byjtech.erp.common.UnderConstructionScreen

@Composable
fun AddCompanyScreen(component: AddCompanyComponent) {
    val nameState by component.nameState.collectAsState()
    val rutState by component.rutState.collectAsState()
    val contactEmailState by component.contactEmailState.collectAsState()
    val companyAdminEmailState by component.companyAdminEmailState.collectAsState()
    val adminNameState by component.adminNameState.collectAsState()
    val isLoading by component.isLoading.collectAsState()
    val isBusy by component.isBusy.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                contentPadding = PaddingValues(bottom = 24.dp)
            ) {
                item {
                    LabeledIconTextField(
                        label = "Nombre",
                        value = nameState.value,
                        onValueChange = component::onNameChanged,
                        errorMessage = nameState.error,
                        leadingIcon = Icons.Default.Business
                    )
                }
                item {
                    LabeledIconTextField(
                        label = "RUT",
                        value = rutState.value,
                        onValueChange = component::onRutChanged,
                        errorMessage = rutState.error,
                        leadingIcon = Icons.Default.Badge
                    )
                }
                item {
                    LabeledIconTextField(
                        label = "Email de contacto",
                        value = contactEmailState.value,
                        onValueChange = component::onContactEmailChanged,
                        errorMessage = contactEmailState.error,
                        leadingIcon = Icons.Default.Email,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                }
                item {
                    LabeledIconTextField(
                        label = "Email del administrador",
                        value = companyAdminEmailState.value,
                        onValueChange = component::onCompanyAdminEmailChanged,
                        errorMessage = companyAdminEmailState.error,
                        leadingIcon = Icons.Default.Email,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
                    )
                }
                item {
                    LabeledIconTextField(
                        label = "Nombre del administrador",
                        value = adminNameState.value,
                        onValueChange = component::onAdminNameChanged,
                        errorMessage = adminNameState.error,
                        leadingIcon = Icons.Default.Person
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = component::onSubmitted,
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(24.dp),
                                color = MaterialTheme.colorScheme.onPrimary,
                                strokeWidth = 3.dp
                            )
                        } else {
                            Text("Crear")
                        }
                    }
                }
            }
        }
    }

    BusyOverlay(isBusy, "Creando...")
}


@Composable
fun LabeledIconTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    errorMessage: String?,
    leadingIcon: ImageVector,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default
) {
    Text(
        text = label,
        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(bottom = 6.dp)
    )
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        isError = errorMessage != null,
        supportingText = {
            errorMessage?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        leadingIcon = { Icon(leadingIcon, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
        keyboardOptions = keyboardOptions,
        singleLine = true
    )
}


