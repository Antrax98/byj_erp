package dev.byjtech.erp

import androidx.compose.material.MaterialTheme
//import androidx.compose.runtime.*

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
import dev.byjtech.erp.core.moduleRoot.RootComponent
import dev.byjtech.erp.core.moduleRoot.nav.home.HomeScreen
import dev.byjtech.erp.core.moduleRoot.nav.auth.LoginScreen
import dev.byjtech.erp.core.moduleRoot.nav.splash.SplashScreen
import dev.byjtech.erp.core.moduleRoot.nav.superHome.SuperHomeScreen


@Composable
fun App(root: RootComponent) {
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