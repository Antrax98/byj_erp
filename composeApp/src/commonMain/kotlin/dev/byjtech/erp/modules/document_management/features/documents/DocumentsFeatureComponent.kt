package dev.byjtech.erp.modules.document_management.features.documents

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.byjtech.erp.common.FeatureComponent
import dev.byjtech.erp.modules.document_management.features.documents.nav.documentsMain.DocumentsMainComponent
import dev.byjtech.erp.modules.document_management.features.documents.nav.documentPage.DocumentPageComponent
import dev.byjtech.erp.modules.document_management.features.documents.nav.addDocument.AddDocumentComponent
import dev.byjtech.erp.modules.document_management.features.documents.nav.editDocument.EditDocumentComponent
import dev.byjtech.erp.modules.document_management.features.documents.nav.documentHistory.DocumentHistoryComponent
import kotlinx.coroutines.flow.StateFlow

interface DocumentsFeatureComponent : FeatureComponent {
    val state: StateFlow<DocumentsFeatureState>
    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        class DocumentsMain(val component: DocumentsMainComponent) : Child()
        class DocumentPage(val component: DocumentPageComponent) : Child()
        class AddDocument(val component: AddDocumentComponent) : Child()
        class EditDocument(val component: EditDocumentComponent) : Child()
        class DocumentHistory(val component: DocumentHistoryComponent) : Child()
    }
}
