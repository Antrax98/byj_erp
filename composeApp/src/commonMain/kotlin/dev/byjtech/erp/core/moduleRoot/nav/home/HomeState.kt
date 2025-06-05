package dev.byjtech.erp.core.moduleRoot.nav.home

data class HomeState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null,
    val isOnListPage: Boolean = true,
)
