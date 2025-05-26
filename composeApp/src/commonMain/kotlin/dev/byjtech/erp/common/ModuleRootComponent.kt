package dev.byjtech.erp.common

import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.common.session.SessionManager


//solo usar en los root de cada modulo
interface ModuleRootComponent {
    val sessionManager: SessionManager
    val apiClient: ApiClient
    val moduleName: String //usar EXACTAMENTE el mismo nombre del modulo en la base de datos
    val toHome: () -> Unit
}