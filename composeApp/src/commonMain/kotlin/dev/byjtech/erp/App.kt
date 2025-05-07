package dev.byjtech.erp

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
//import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import byj_erp.composeapp.generated.resources.Res
import byj_erp.composeapp.generated.resources.compose_multiplatform
import dev.byjtech.erp.session.SessionManager

//@Composable
//@Preview
//fun App(navController: NavHostController, sessionManager: SessionManager) {
//    MaterialTheme {
//        var showContent by remember { mutableStateOf(false) }
//        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }
//    }
//}

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import dev.byjtech.erp.navigation.RootComponent
import dev.byjtech.erp.ui.HomeScreen
import dev.byjtech.erp.ui.LoginScreen
import dev.byjtech.erp.ui.SplashScreen
import dev.byjtech.erp.ui.SuperHomeScreen


@Composable
fun App(root: RootComponent) { //se cambia el nombre a App y se le pasa como parametro RootComponent
    MaterialTheme {
        Children(
            stack = root.childStack,
            animation = stackAnimation(slide())
        ) {
            when (val child = it.instance) {
                is RootComponent.Child.Home -> HomeScreen(child.component)
                is RootComponent.Child.Login -> LoginScreen(child.component)
                is RootComponent.Child.Splash -> SplashScreen()
                is RootComponent.Child.SuperHome -> SuperHomeScreen(child.component)
            }
        }
    }
}