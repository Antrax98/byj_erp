package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companyPage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.common.UnderConstructionScreen
import dev.byjtech.erp.core.dto.CompanyDTO
import dev.byjtech.erp.core.dto.SubscriptionDTO

@Composable
fun CompanyPageScreen(component: CompanyPageComponent) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp)
    ) {
        item { CompanyInfoCard(company = component.company) }
        item { BillingInfoCard() }
        item { SubscriptionsList(dummySubscriptions) }
    }
}


@Composable
fun CompanyInfoCard(company: CompanyDTO) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(modifier = Modifier.padding(20.dp)) {
            Icon(
                imageVector = Icons.Default.Business,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier
                    .size(64.dp)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), CircleShape)
                    .padding(16.dp)
            )
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = company.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = company.contactEmail,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Creado: ${company.createdAt}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}


@Composable
fun BillingInfoCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Información de Facturación",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            Text("Último pago: 10/10/2023")
        }
    }
}


val dummySubscriptions = listOf(
    SubscriptionDTO(id = 1, companyId = 100, moduleId = 1, isActive = true, isAccessible = true),
    SubscriptionDTO(id = 2, companyId = 100, moduleId = 2, isActive = true, isAccessible = false),
    SubscriptionDTO(id = 3, companyId = 100, moduleId = 3, isActive = false, isAccessible = false),
    SubscriptionDTO(id = 4, companyId = 100, moduleId = 1, isActive = true, isAccessible = true),
    SubscriptionDTO(id = 5, companyId = 100, moduleId = 2, isActive = false, isAccessible = true),
    SubscriptionDTO(id = 6, companyId = 100, moduleId = 3, isActive = true, isAccessible = true)
)

@Composable
fun SubscriptionsList(subscriptions: List<SubscriptionDTO>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = "Suscripciones",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Divider(modifier = Modifier.padding(vertical = 8.dp))

            if (subscriptions.isEmpty()) {
                Text(
                    text = "No hay suscripciones activas",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )
            } else {
                subscriptions.forEach { subscription ->
                    SubscriptionCard(subscription)
                }
            }
        }
    }
}


@Composable
fun SubscriptionCard(subscription: SubscriptionDTO) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Módulo ID: ${subscription.moduleId}",
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (subscription.isActive) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    contentDescription = "Activo",
                    tint = if (subscription.isActive) Color(0xFF388E3C) else Color(0xFFD32F2F)
                )
                Spacer(Modifier.width(8.dp))
                Text("Activo: ${if (subscription.isActive) "Sí" else "No"}")
            }
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = if (subscription.isAccessible) Icons.Default.CheckCircle else Icons.Default.Cancel,
                    contentDescription = "Accesible",
                    tint = if (subscription.isAccessible) Color(0xFF1976D2) else Color(0xFF757575)
                )
                Spacer(Modifier.width(8.dp))
                Text("Accesible: ${if (subscription.isAccessible) "Sí" else "No"}")
            }
        }
    }
}

