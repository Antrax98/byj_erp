package dev.byjtech.erp.core.moduleRoot.nav.superHome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addCompany.AddCompanyScreen
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.addSubscription.AddSubscriptionScreen
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companies.CompaniesScreen
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companyPage.CompanyPageScreen
import dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.superHomeMain.SuperHomeMainScreen
import dev.byjtech.erp.modules.document_management.features.documents.DocumentsFeatureScreen
import dev.byjtech.erp.modules.document_management.features.editHistory.EditHistoryFeatureScreen
import dev.byjtech.erp.modules.document_management.features.auditLogs.AuditLogsFeatureScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperHomeScreen(component: SuperHomeComponent) {
    val dummyNames = listOf(
        "Users", "Companies", "Modules",
        "Subscriptions", "Billings", "SuperAdmins"
    )

    val isOnMainPage by component.isOnMainPage.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Añadir modulo") },
                navigationIcon = {
                    if (isOnMainPage) {
                        IconButton(onClick = { /* Espacio reservado */ }, enabled = false) {
                            Icon(
                                Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null,
                                modifier = Modifier.alpha(0f)
                            )
                        }
                    } else {
                        IconButton(onClick = { component.onBack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
                        }
                    }
                },
                actions = {
                    DropdownButtonMenu(component)
                },
                colors = TopAppBarDefaults.topAppBarColors()
            )
        },
        content = { innerPadding ->
            Children(
                stack = component.childStack,
                modifier = Modifier.padding(innerPadding),
                animation = stackAnimation(slide())
            ){ entry ->
                when (val child = entry.instance) {
                    is SuperHomeComponent.Child.Companies -> {
                        CompaniesScreen(child.component)
                    }
                    is SuperHomeComponent.Child.Main -> {
                        SuperHomeMainScreen(child.component)
                    }
                    is SuperHomeComponent.Child.AddCompany -> {
                        AddCompanyScreen(child.component)
                    }
                    is SuperHomeComponent.Child.CompanyPage -> {
                        CompanyPageScreen(child.component)
                    }
                    is SuperHomeComponent.Child.AddSubscription -> {
                        AddSubscriptionScreen(child.component)
                    }
                    is SuperHomeComponent.Child.DocumentsFeature -> {
                        DocumentsFeatureScreen(child.component)
                    }
                    is SuperHomeComponent.Child.EditHistoryFeature -> {
                        EditHistoryFeatureScreen(child.component)
                    }
                    is SuperHomeComponent.Child.AuditLogsFeature -> {
                        AuditLogsFeatureScreen(child.component)
                    }
                }
            }
        }
    )
}

//se podria usar el mismo que HomeScreen pero requeriria un componente con interfases compartidas
//y que ambos implementen logout y settings
@Composable
fun DropdownButtonMenu(component: SuperHomeComponent) {
    var expanded by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    Box {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Filled.MoreVert,
                contentDescription = "Configurations",
                modifier = Modifier.size(40.dp)
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                onClick = { expanded = false },
                text = { Text("Settings") },
                leadingIcon = {
                    Icon(Icons.Filled.Settings, contentDescription = "settings")
                }
            )
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    showLogoutDialog = true
                },
                text = { Text("Logout") },
                leadingIcon = {
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "log out")
                }
            )
        }

        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                title = { Text("¿Cerrar sesión?") },
                text = { Text("¿Estás seguro de que quieres cerrar sesión?") },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showLogoutDialog = false
                            coroutineScope.launch {
                                component.onLogout()
                            }
                        }
                    ) {
                        Text("Sí")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Cancelar")
                    }
                }
            )
        }
    }
}
