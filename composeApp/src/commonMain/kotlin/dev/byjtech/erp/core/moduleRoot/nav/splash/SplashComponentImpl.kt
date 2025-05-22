package dev.byjtech.erp.core.moduleRoot.nav.splash

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.common.session.SessionManager
import kotlinx.coroutines.*
import com.arkivanov.essenty.lifecycle.*

class SplashComponentImpl(
    componentContext: ComponentContext,
    private val sessionManager: SessionManager
) : SplashComponent, ComponentContext by componentContext {

    // Crea un CoroutineScope dentro del componente
    private val scope = CoroutineScope(Dispatchers.Main + SupervisorJob())

    init {
        // Lanza la coroutine de manera adecuada
        scope.launch {
            val sessionJob = async {sessionManager.loadSession()}
            sessionJob.await()
        }

        ////PARECE QUE ESTO NICIQUIERA ERA NECESARIO, todo: porbar eliminandolo
        // Cancela el scope cuando el componente se destruye
        lifecycle.doOnDestroy {
            scope.cancel() // Cancela las corutinas cuando el componente es destruido
        }
    }

}