package dev.byjtech.erp.modules.document_management.features.documents.nav.documentsMain

import kotlinx.coroutines.flow.StateFlow
import dev.byjtech.erp.modules.document_management.features.documents.DocumentsFeatureComponentImpl.Config

interface DocumentsMainHomeComponent {
    fun navTo(config: Config)
}
