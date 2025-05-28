package dev.byjtech.erp.core.moduleRoot.nav.home

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.backhandler.BackCallback
import com.arkivanov.essenty.lifecycle.subscribe
import dev.byjtech.erp.common.ModuleManager
import dev.byjtech.erp.common.ModuleRootComponent
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.common.session.SessionManager
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.moduleList.ModuleListComponentImpl
import kotlinx.coroutines.flow.*

class HomeComponentImpl(
    componentContext: ComponentContext,
    private val sessionManager: SessionManager,
    private val api: ApiClient,
    override val moduleManager: ModuleManager
): HomeComponent, ComponentContext by componentContext {

//    //TODO: MOVER A UN ARCHIVO COMPARTIDO este sealed class
//    sealed class HomeNavEvent {
//        data class NavigateTo(val target: String) : HomeNavEvent()
//        data object ToHome : HomeNavEvent()
//    }

    //override val moduleName = "core" // este no es realmente necesario, es solo por pruebas

//    private val _events = MutableSharedFlow<HomeNavEvent>()
//    val events: SharedFlow<HomeNavEvent> = _events

    private val _state = MutableStateFlow(HomeState())
    override val state: StateFlow<HomeState> = _state.asStateFlow()

    override suspend fun onLogout() {
        _state.update { it.copy(isLoading = true) }
        try {
            sessionManager.logout()
            _state.update { it.copy(isLoading = false, message = "Sesión cerrada") }
        } catch (e: Exception) {
            _state.update { it.copy(isLoading = false, error = e.message) }
        }
    }

    override suspend fun onTestClick() {
        _state.update { it.copy(isLoading = true) }
        try {
            api.coreAuth.test()
            _state.update { it.copy(isLoading = false, message = "Petición exitosa") }
        } catch (e: Exception) {
            _state.update { it.copy(isLoading = false, error = e.message) }
        }
    }

    //NAVEGACION!!!!
    override val entriesByName = moduleManager.entriesByName()
    private val navigation = StackNavigation<String>()

    private fun childFactory(config: String, componentContext: ComponentContext): ModuleRootComponent {
        return when (config) {
            "home" -> ModuleListComponentImpl(
                componentContext.childContext("home_moduleList"),
                sessionManager.userPermissions,
                api,
                toHome = ::toHome,
                navTo = ::navigateTo,
                modulesMetadata = moduleManager.metadataMap()
            )
            else -> {
                val factory = entriesByName[config]?.factory
                    ?: throw IllegalArgumentException("Invalid config: $config")

                factory.create(componentContext.childContext(config), sessionManager.userPermissions, api, ::toHome)
            }
        }
    }


    private val stack = childStack(
        source = navigation,
        serializer = null,
        initialStack = { listOf("home") },
        handleBackButton = true,
        childFactory = ::childFactory
    )

    override val childStack: Value<ChildStack<*, ModuleRootComponent>> = stack

    //solo usable por el ModuleList
    private fun navigateTo(target: String) {
        val current = childStack.value.active.configuration
        if (current != target) {
            navigation.replaceCurrent(target)
        }
    }
    private fun toHome() {
        navigation.replaceCurrent("home")
    }



    /*
    manejando el back button
    si el child actual es distinto a "home", se reemplaza por "home" y se consume el evento
    por otro lado, si el child actual es "home", se deja pasar el evento al flujo normal
    que lo mas probable es salir de la app

    si no funciona el que se NO se consuma el event
    hay que injectarle al home una funcion del root para cerrar la App

    de esto funcionar, el darle toHome() a los componentes seria redundante
    
    link: https://arkivanov.github.io/Decompose/component/back-button/


    se supone que el componente con un childStack mas resiente se encarga del Backcall
    pero si este ya no puede hacer pop (solo le queda un child en el stack),
    entonses le delega el evento al parent, en este caso Home (this)
     */
    private val backCallback = BackCallback(
        onBack = {
            if (childStack.value.active.configuration != "home") {
                navigation.replaceCurrent("home")
                //se consumio el evento???
            } else {
               // no consumió, sigue el flujo normal??????
            }
        }
    )

    init {
        backHandler.register(backCallback)

        lifecycle.subscribe(
            onDestroy = {
                backHandler.unregister(backCallback)
            }
        )
    }
}