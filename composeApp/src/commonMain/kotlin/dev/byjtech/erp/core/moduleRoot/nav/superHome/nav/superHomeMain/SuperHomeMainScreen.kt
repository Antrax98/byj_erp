package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.superHomeMain

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.core.moduleRoot.nav.superHome.SuperHomeComponentImpl.Config

//@Composable
//fun SuperHomeMainScreen(component: SuperHomeMainComponent) {
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        LazyVerticalGrid(
//            columns = GridCells.Fixed(2),
//            modifier = Modifier.fillMaxSize(),
//            verticalArrangement = Arrangement.spacedBy(12.dp),
//            horizontalArrangement = Arrangement.spacedBy(12.dp),
//            contentPadding = PaddingValues(bottom = 100.dp)
//        ) {
//            item {
//                NavigationCard("Empresas", "Ver listado de empresas") {
//                    component.navTo(Config.Companies)
//                }
//            }
//
//            items((1..3).toList()) { i ->
//                NavigationCard("Test $i", "Boton de prueba $i") {
//                    println("Test $i pressed")
//                }
//            }
//        }
//    }
//}

@Composable
fun SuperHomeMainScreen(component: SuperHomeMainComponent) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 100.dp)
        ) {
            item {
                NavigationCard(
                    title = "Empresas",
                    subtitle = "Ver listado de empresas",
                    icon = Icons.Default.Business
                ) {
                    component.navTo(Config.Companies)
                }
            }

            item {
                NavigationCard("Gestión Documentos", "Administrar documentos del sistema") {
                    component.navTo(Config.DocumentsFeature)
                }
            }

            item {
                NavigationCard(
                    title = "Test de roles",
                    subtitle = "Pantalla para probar roles",
                    icon = Icons.Default.Security
                ) {
                    println("Test Roles")
                }
            }

            item {
                NavigationCard(
                    title = "Test de permisos",
                    subtitle = "Pantalla para probar permisos",
                    icon = Icons.Default.Settings
                ) {
                    println("Test Permisos")
                }
            }

            item {
                NavigationCard(
                    title = "Test de navegación",
                    subtitle = "Ver cómo se comporta la navegación",
                    icon = Icons.Default.Explore
                ) {
                    println("Test Navegación")
                }
            }
        }
    }
}



//@Composable
//fun NavigationCard(
//    title: String,
//    subtitle: String? = null,
//    onClick: () -> Unit
//) {
//    Card(
//        onClick = onClick,
//        shape = RoundedCornerShape(16.dp),
//        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
//        modifier = Modifier.fillMaxWidth()
//    ) {
//        Row(
//            modifier = Modifier
//                .padding(16.dp),
//            verticalAlignment = Alignment.CenterVertically
//        ) {
//            Column(modifier = Modifier.weight(1f)) {
//                Text(
//                    text = title,
//                    style = MaterialTheme.typography.titleMedium
//                )
//                if (subtitle != null) {
//                    Spacer(modifier = Modifier.height(4.dp))
//                    Text(
//                        text = subtitle,
//                        style = MaterialTheme.typography.bodyMedium,
//                        color = MaterialTheme.colorScheme.onSurfaceVariant
//                    )
//                }
//            }
//            Icon(
//                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
//                contentDescription = "Ir",
//                tint = MaterialTheme.colorScheme.primary
//            )
//        }
//    }
//}

@Composable
fun NavigationCard(
    title: String,
    subtitle: String? = null,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Card Icon",
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                        shape = CircleShape
                    )
                    .padding(8.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            subtitle?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "Ir",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
