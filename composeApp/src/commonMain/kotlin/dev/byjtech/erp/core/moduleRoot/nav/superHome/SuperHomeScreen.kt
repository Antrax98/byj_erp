package dev.byjtech.erp.core.moduleRoot.nav.superHome

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuperHomeScreen(component: SuperHomeComponent){
    val coroutineScope = rememberCoroutineScope()

    Column (
        modifier = Modifier
            .fillMaxSize(),
            //.padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        TopAppBar(
            navigationIcon = {
                IconButton(onClick = {
                    coroutineScope.launch { component.onLogout() }
                }){
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout")
                }
            },
            title = { Text("SuperHome") },
            colors = TopAppBarDefaults.topAppBarColors()
        )

        Text(text = "Welcome to Super Home Screen")

        val dummyNames = listOf(
            "users", "companies", "Modules",
            "Subscriptions", "Bilings", "SuperAdmins"
        )

        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 120.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            //modifier = Modifier.fillMaxSize()
        ) {
            items(dummyNames) { name ->
                Button(
                    onClick = { /*NADA*/ },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(name)
                }
            }
        }

        Button(onClick = {
            // Llamar a la función suspendida dentro de una coroutine
            coroutineScope.launch {
                try {
                    component.onTestClick()
                } catch (e: Exception) {
                    println("Error: $e")
                }
            }
        }) {
            Text(text = "Test")
        }
    }
}