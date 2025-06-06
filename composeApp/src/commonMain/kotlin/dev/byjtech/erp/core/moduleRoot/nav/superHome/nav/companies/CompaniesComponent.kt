package dev.byjtech.erp.core.moduleRoot.nav.superHome.nav.companies

import kotlinx.coroutines.flow.StateFlow

interface CompaniesComponent {
    val state: StateFlow<CompaniesState>
}