package dev.byjtech.erp.core.home

data class HomeState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val message: String? = null

)
