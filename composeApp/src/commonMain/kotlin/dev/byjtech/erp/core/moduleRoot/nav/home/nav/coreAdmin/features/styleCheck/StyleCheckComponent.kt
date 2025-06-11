package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.styleCheck

import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.flow.StateFlow

interface StyleCheckComponent {
    val state: StateFlow<StyleCheckState>
}