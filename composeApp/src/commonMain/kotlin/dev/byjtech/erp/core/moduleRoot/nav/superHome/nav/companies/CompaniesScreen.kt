package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companies

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Settings
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.common.ErrorWithRetry
import dev.byjtech.erp.common.UnderConstructionScreen
import dev.byjtech.erp.core.dto.CompanyDTO
import dev.byjtech.erp.core.dto.ModuleDTO
import dev.byjtech.erp.core.moduleRoot.nav.superHome.SuperHomeComponentImpl
import kotlinx.datetime.toKotlinLocalDateTime
import java.time.LocalDateTime

@Composable
fun CompaniesScreen(component: CompaniesComponent) {
    val state by component.state.collectAsState()
    val companies by component.companiesList.collectAsState()
    val isLoading by component.isLoading.collectAsState()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { component.navTo(SuperHomeComponentImpl.Config.AddCompany) },
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Agregar empresa",
                    tint = Color.White
                )
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            if(isLoading){
                CircularProgressIndicator()
            }else{
                if(companies != null){
                    LazyColumn {
                        companies!!.forEach { company ->
                            item {
                                CompanyContainer(company){component.navTo(SuperHomeComponentImpl.Config.CompanyPage(company))}
                            }
                        }
                        item {
                            Spacer(modifier = Modifier.height(100.dp))
                        }
                    }
                }else{
                    ErrorWithRetry(
                        "No hay empresas",
                        "No se encontraron empresas",
                        onRetry = {
                            println("reintentando buscar empresas nuevamente")
                            component.loadCompanies()
                        }
                    )
                }
            }
        }

    }

//    ModulesListScreen(testModuleDTOs) { module ->
//        println("Agregar módulo: ${module.name}")
//    }
}

@Composable
fun CompanyContainer(company: CompanyDTO, onClick: () -> Unit = {}) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Business,
                contentDescription = "Ícono Empresa",
                modifier = Modifier
                    .size(64.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f))
                    .padding(16.dp),
                tint = MaterialTheme.colorScheme.primary
            )
            Column(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .weight(1f)
            ) {
                Text(
                    text = company.name,
                    style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = company.rut,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(
                onClick = onClick,
                modifier = Modifier.padding(start = 8.dp)
            ) {
                Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Ver detalles",
                tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

val testCompanies = listOf(
    CompanyDTO(
        id = "1",
        name = "ByJtech",
        rut = "12.345.678-9",
        contactEmail = "test@byjtech.com",
        billingId = "BILL-001",
        createdAt = java.time.LocalDateTime.now().minusMonths(3).toKotlinLocalDateTime(),
        updatedAt = java.time.LocalDateTime.now().toKotlinLocalDateTime()
    ),
    CompanyDTO(
        id = "2",
        name = "TechNova",
        rut = "98.765.432-1",
        contactEmail = "contact@technova.com",
        billingId = "BILL-002",
        createdAt = java.time.LocalDateTime.now().minusMonths(2).toKotlinLocalDateTime(),
        updatedAt = java.time.LocalDateTime.now().toKotlinLocalDateTime()
    ),
    CompanyDTO(
        id = "3",
        name = "GreenCode",
        rut = "11.223.344-5",
        contactEmail = "hello@greencode.cl",
        billingId = null,
        createdAt = java.time.LocalDateTime.now().minusMonths(1).toKotlinLocalDateTime(),
        updatedAt = java.time.LocalDateTime.now().minusDays(10).toKotlinLocalDateTime()
    ),
    CompanyDTO(
        id = "4",
        name = "Softlink Ltda.",
        rut = "76.543.210-3",
        contactEmail = "info@softlink.cl",
        billingId = "BILL-004",
        createdAt = java.time.LocalDateTime.now().minusWeeks(8).toKotlinLocalDateTime(),
        updatedAt = java.time.LocalDateTime.now().minusDays(5).toKotlinLocalDateTime()
    ),
    CompanyDTO(
        id = "5",
        name = "Nova Solutions",
        rut = "22.333.444-6",
        contactEmail = "support@novasolutions.io",
        billingId = "BILL-005",
        createdAt = java.time.LocalDateTime.now().minusDays(40).toKotlinLocalDateTime(),
        updatedAt = java.time.LocalDateTime.now().minusDays(1).toKotlinLocalDateTime()
    ),
    CompanyDTO(
        id = "6",
        name = "AndesCloud",
        rut = "55.666.777-8",
        contactEmail = "ventas@andescloud.cl",
        billingId = null,
        createdAt = java.time.LocalDateTime.now().minusMonths(5).toKotlinLocalDateTime(),
        updatedAt = java.time.LocalDateTime.now().minusWeeks(2).toKotlinLocalDateTime()
    )
)


val testModuleDTOs = listOf(
    ModuleDTO(
        id = "a1b2c3d4-e5f6-7890-abcd-1234567890ab",
        name = "core",
        displayName = "Core",
        description = "Módulo principal del sistema",
        developerOnly = false
    ),
    ModuleDTO(
        id = "b2c3d4e5-f678-9012-abcd-2345678901bc",
        name = "billing",
        displayName = "Facturación",
        description = "Módulo para la gestión de facturas y pagos",
        developerOnly = false
    ),
    ModuleDTO(
        id = "c3d4e5f6-7890-1234-abcd-3456789012cd",
        name = "inventario",
        displayName = "Inventario",
        description = "Módulo para gestión de inventarios y stock",
        developerOnly = false
    ),
    ModuleDTO(
        id = "d4e5f678-9012-3456-abcd-4567890123de",
        name = "patentes",
        displayName = "Patentes",
        description = "Modulo para la gestión de patentes",
        developerOnly = false
    ),

)

///TEST

@Composable
fun ModulesListScreen(
    modules: List<ModuleDTO>,
    onAddModule: (ModuleDTO) -> Unit = {}
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(modules) { module ->
            ModuleCard(module, onAddModule)
        }
    }
}

@Composable
fun ModuleCard(
    module: ModuleDTO,
    onAddModule: (ModuleDTO) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Icono dentro de círculo
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Module Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = module.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = module.displayName,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = module.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            if (module.name.lowercase() == "core") {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Core module added",
                    tint = Color(0xFF388E3C),
                    modifier = Modifier.size(28.dp)
                )
            } else {
                Icon(
                    modifier = Modifier
                        .size(28.dp)
                        .clickable { onAddModule(module) },
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add module",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

