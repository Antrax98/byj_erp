package dev.byjtech.erp.navigation

import com.arkivanov.decompose.ComponentContext
import dev.byjtech.erp.session.SessionManager

interface HomeComponent{
    suspend fun onLogout()
    suspend fun onTestClick()
}