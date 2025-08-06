package dev.byjtech.erp.modules.document_management.features.documents.nav.documentsMain

import dev.byjtech.erp.modules.document_management.features.documents.DocumentsFeatureComponentImpl.Config

class DocumentsMainHomeComponentImpl(
    private val navTo: (Config) -> Unit
) : DocumentsMainHomeComponent {
    
    override fun navTo(config: Config) {
        navTo.invoke(config)
    }
}
