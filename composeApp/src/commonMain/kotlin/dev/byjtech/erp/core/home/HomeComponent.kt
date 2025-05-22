package dev.byjtech.erp.core.home

import kotlinx.coroutines.flow.StateFlow

interface HomeComponent{
    val state: StateFlow<HomeState>
    suspend fun onLogout()
    suspend fun onTestClick()
}