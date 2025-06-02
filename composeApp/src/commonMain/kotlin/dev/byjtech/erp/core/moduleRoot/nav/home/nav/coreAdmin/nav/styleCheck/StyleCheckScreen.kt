package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.nav.styleCheck

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun StyleCheckScreen(component: StyleCheckComponent) {
    val state by component.state.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    Column {
        Text("STYLE CHECK")
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