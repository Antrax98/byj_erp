package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@Composable
fun CoreAdminRootScreen(component: CoreAdminRootComponentOld) {
    val state by component.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("TEXTO DEL CORE ADMIN ROOT")
        Button(onClick = { coroutineScope.launch { component.changeLoading() }}) {
            Text("BOTON TODO()")
        }
        if (state.isLoading) {
            CircularProgressIndicator()
        }
        Button(onClick = { component.toHome() }){
            Text("to Home")
        }

        Text("Lista de Features")

        val dummyNames = listOf(
            "users", "company", "modules",
            "roles", "billing", "etc..."
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(dummyNames) { name ->
                Button(
                    onClick = { /* No hace nada */ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(name)
                }
            }
        }

        ThemePreviewScreen()

    }
}

@Composable
fun ThemePreviewScreen() {
    val colors = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Primary", color = colors.onPrimary, modifier = Modifier
            .fillMaxWidth()
            .background(colors.primary)
            .padding(8.dp))

        Text("Secondary", color = colors.onSecondary, modifier = Modifier
            .fillMaxWidth()
            .background(colors.secondary)
            .padding(8.dp))

        Text("Tertiary", color = colors.onTertiary, modifier = Modifier
            .fillMaxWidth()
            .background(colors.tertiary)
            .padding(8.dp))

        Text("Surface", color = colors.onSurface, modifier = Modifier
            .fillMaxWidth()
            .background(colors.surface)
            .padding(8.dp))

        Text("Background", color = colors.onBackground, modifier = Modifier
            .fillMaxWidth()
            .background(colors.background)
            .padding(8.dp))

        Button(onClick = {}) {
            Text("Este botón usa primary")
        }
    }
}