package dev.byjtech.erp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.byjtech.erp.navigation.HomeComponent
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(component: HomeComponent) {
    val coroutineScope = rememberCoroutineScope()

    Column (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(text = "Welcome to Home Screen")

        Button(onClick = {
            // Llamar a la función suspendida dentro de una coroutine
            coroutineScope.launch {
                try {
                    component.onLogout()
                } catch (e: Exception) {
                    println("Error: $e")
                }
            }
        }) {
            Text(text = "Logout")
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