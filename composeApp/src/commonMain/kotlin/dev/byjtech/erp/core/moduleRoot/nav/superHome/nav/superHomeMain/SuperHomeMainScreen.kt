package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.superHomeMain

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.byjtech.erp.core.moduleRoot.nav.superHome.SuperHomeComponentImpl.Config

@Composable
fun SuperHomeMainScreen(component: SuperHomeMainComponent) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            item {
                Button(onClick = { component.navTo(Config.Companies) }) {
                    Text("Companies")
                }
            }
            //Aqui Agregar botones para la navegacion de despues
            for (i in 1..10) {
                item {
                    Button(onClick = { println("Test $i pressed") }) {
                        Text("Test $i")
                    }
                }
            }
        }
    }
}