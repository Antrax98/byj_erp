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
import dev.byjtech.erp.common.ComponentConfig
import dev.byjtech.erp.common.FeatureComponent
import dev.byjtech.erp.common.ModuleManager
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.common.session.SessionManager
import dev.byjtech.erp.core.dto.UserDTO
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.featuresList.FeatureListComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.roles.RolesFeatureComponentImpl
import dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.UsersFeatureComponentImpl
import kotlinx.coroutines.flow.*
import kotlin.system.exitProcess

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

    override val actualUser: StateFlow<UserDTO?> = sessionManager.actualUser

    private val _isOnMainPage = MutableStateFlow(true)
    override val isOnMainPage: StateFlow<Boolean> = _isOnMainPage.asStateFlow()

    private val _titleState = MutableStateFlow<String>("Home")
    override val titleState: StateFlow<String> = _titleState.asStateFlow()

    private fun updateTitle(newTitle: String) {
        _titleState.value = newTitle
    }

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

    //NAVEGACION NEW

    private val navigation = StackNavigation<ComponentConfig>()

    //child factory
    private val childMap = moduleManager.featureFactoryMap(sessionManager.userPermissions.value, sessionManager.allowedModules.value)
    fun childFactory(config: ComponentConfig, componentContext: ComponentContext): FeatureComponent {
        return when (config) {
            ComponentConfig("core", "home") -> FeatureListComponentImpl(
                componentContext.childContext("home_featureList"),
                userPermissions = sessionManager.userPermissions,
                apiClient = api,
                toHome = ::toHome,
                navTo = ::navigateTo,
                buttonsMap = moduleManager.buttonMap(sessionManager.userPermissions.value, sessionManager.allowedModules.value),
                updateTitle = ::updateTitle
            )
            //aqui poner mas featreComponents indispensables
            ComponentConfig("core", "roles") -> RolesFeatureComponentImpl(
                componentContext.childContext("roles"),
                userPermissions = sessionManager.userPermissions,
                apiClient = api,
                toHome = ::toHome,
                sessionManagerRef=sessionManager,
                updateTitle = ::updateTitle
            )
            ComponentConfig("core", "users") -> UsersFeatureComponentImpl(
                componentContext.childContext("users"),
                userPermissions = sessionManager.userPermissions,
                apiClient = api,
                sessionManagerRef=sessionManager,
                toHome = ::toHome,
                updateTitle = ::updateTitle
            )

            else -> {
                val factory = childMap[config.module]?.get(config.feature)
                    ?: throw IllegalArgumentException("Invalid config: $config")
                factory.create(componentContext.childContext(config.toString()), sessionManager.userPermissions, api, ::toHome, ::updateTitle)
            }
        }
    }

    private val stack = childStack(
        source = navigation,
        serializer = ComponentConfig.serializer(),
        initialStack = { listOf(ComponentConfig("core", "home")) },
        handleBackButton = true,
        childFactory = ::childFactory
    )

    override val childStack: Value<ChildStack<ComponentConfig, FeatureComponent>> = stack

    private fun navigateTo(target: ComponentConfig) {
        val current = childStack.value.active.configuration
        if (current != target) {
            _state.update { it.copy(isOnListPage = false) }
            navigation.replaceCurrent(target)
        }
    }
    private fun toHome() {
        println("navegando a home (featureList)")
        updateTitle("Home")
        _state.update { it.copy(isOnListPage = true) }
        navigation.replaceCurrent(ComponentConfig("core", "home"))
    }

    override fun onBack() {
        val current = childStack.value.active
        if (current.configuration != ComponentConfig("core", "home")) {
            if(!current.instance.onBack()){
                println("navegando a home (featureList)")
                toHome()
            }
        } else {
            //exitProcess(0)
            println("se intenta salir de la app")
        }
    }

//    override val screenMap = moduleManager.screenMap(sessionManager.userPermissions.value, sessionManager.allowedModules.value)
    override val screenMap = moduleManager.screenMap()

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

    //descomentar al terminar la navegacion de este componente
    private val backCallback = BackCallback(
        onBack = {
            if (childStack.value.active.configuration != ComponentConfig("core", "home")) {
                println("Back button pressed")
                navigation.replaceCurrent(ComponentConfig("core", "home"))
            } else {
                println("Cerrando aplicacion")
                exitProcess(0)
                //esto supuestamente no esta recomendado, pero no se como solucionarlo
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