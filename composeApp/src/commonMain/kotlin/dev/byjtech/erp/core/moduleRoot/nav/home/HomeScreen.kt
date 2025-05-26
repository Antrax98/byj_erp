package dev.byjtech.erp.core.moduleRoot.nav.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import dev.byjtech.erp.common.ModuleRootComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.CoreAdminRootComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.CoreAdminRootScreen
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.moduleList.ModuleListComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.moduleList.ModuleListScreen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(component: HomeComponent) {
    val state by component.state.collectAsState()

    val coroutineScope = rememberCoroutineScope()
    Column (
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

//        Row (
//            modifier = Modifier
//                .fillMaxWidth()
//                .background(Color.Blue)
//            ,
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceAround
//        ){
//            Text("test bar")
//
//            if (state.isLoading) {
//                CircularProgressIndicator()
//            }
//
//            state.error?.let {
//                Text("Error: $it", color = Color.Red)
//            }
//
//            state.message?.let {
//                Text("Mensaje: $it", color = Color.Green)
//            }
//
//            Button(onClick = {
//                coroutineScope.launch { component.onLogout() }
//            }) {
//                Text("Logout")
//            }
//
//            Button(onClick = {
//                coroutineScope.launch { component.onTestClick() }
//            }) {
//                Text("Test")
//            }
//        }

        TopAppBar(
            navigationIcon = {
                IconButton(onClick = {
                    coroutineScope.launch { component.onLogout() }
                }){
                    Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = "Logout")
                }
            },
            title = { Text("Home") },
            colors = TopAppBarDefaults.topAppBarColors()
        )

        Children(
            stack = component.childStack,
            animation = stackAnimation(slide())
        ) { entry ->
            when (val config = entry.configuration) {
                "home" -> ModuleListScreen(entry.instance as ModuleListComponent)
                else -> {
                    val moduleEntry = component.entriesByName[config]
                    val componente = entry.instance as? ModuleRootComponent
                    if (moduleEntry != null && componente != null) {
                        moduleEntry.renderScreen(componente)
                    } else {
                        Text("Unknown module: $config")
                    }
                }
            }
        }
    }


}