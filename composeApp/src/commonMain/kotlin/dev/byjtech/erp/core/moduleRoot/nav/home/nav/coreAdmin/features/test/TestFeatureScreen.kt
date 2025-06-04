package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.test

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun TestFeatureScreen (component: TestFeatureComponent) {
    val state by component.state.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("test feature screen")
        Button(onClick = { component.toHome()}){
            Text("volver a home")
        }
    }

}