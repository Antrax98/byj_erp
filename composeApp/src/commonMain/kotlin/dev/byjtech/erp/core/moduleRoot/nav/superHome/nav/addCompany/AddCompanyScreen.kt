package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addCompany

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.common.UnderConstructionScreen

@Composable
fun AddCompanyScreen(component: AddCompanyComponent) {
    val nameState by component.nameState.collectAsState()
    val contactEmailState by component.contactEmailState.collectAsState()
    val companyAdminEmailState by component.companyAdminEmailState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            elevation = CardDefaults.cardElevation(8.dp),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    value = nameState.value,
                    onValueChange = component::onNameChanged,
                    label = { Text("Nombre") },
                    isError = nameState.error != null,
                    supportingText = {
                        nameState.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = contactEmailState.value,
                    onValueChange = component::onContactEmailChanged,
                    label = { Text("Email de contacto") },
                    isError = contactEmailState.error != null,
                    supportingText = {
                        contactEmailState.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = companyAdminEmailState.value,
                    onValueChange = component::onCompanyAdminEmailChanged,
                    label = { Text("Email del administrador") },
                    isError = companyAdminEmailState.error != null,
                    supportingText = {
                        companyAdminEmailState.error?.let { Text(it, color = MaterialTheme.colorScheme.error) }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = component::onSubmitted,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Crear")
                }
            }
        }
    }
}

