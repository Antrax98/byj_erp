package dev.byjtech.erp.modules.document_management.features.documents

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import dev.byjtech.erp.common.FeatureComponent
import dev.byjtech.erp.modules.document_management.features.documents.nav.documentsMain.DocumentsMainComponent
import dev.byjtech.erp.modules.document_management.features.documents.nav.documentsMain.DocumentsMainHomeComponent
import dev.byjtech.erp.modules.document_management.features.documents.nav.documentPage.DocumentPageComponent
import dev.byjtech.erp.modules.document_management.features.documents.nav.addDocument.AddDocumentComponent
import dev.byjtech.erp.modules.document_management.features.documents.nav.editDocument.EditDocumentComponent
import dev.byjtech.erp.modules.document_management.features.documents.nav.documentHistory.DocumentHistoryComponent
import dev.byjtech.erp.modules.document_management.features.notifications.NotificationsComponent
import dev.byjtech.erp.modules.document_management.features.notifications.NotificationSettingsComponent
import kotlinx.coroutines.flow.StateFlow

interface DocumentsFeatureComponent : FeatureComponent {
    val state: StateFlow<DocumentsFeatureState>
    val childStack: Value<ChildStack<*, Child>>

    sealed class Child {
        class DocumentsHome(val component: DocumentsMainHomeComponent) : Child()
        class DocumentsList(val component: DocumentsMainComponent) : Child()
        class DocumentPage(val component: DocumentPageComponent) : Child()
        class AddDocument(val component: AddDocumentComponent) : Child()
        class EditDocument(val component: EditDocumentComponent) : Child()
        class DocumentHistory(val component: DocumentHistoryComponent) : Child()
        class Notifications(val component: NotificationsComponent) : Child()
        class NotificationSettings(val component: NotificationSettingsComponent) : Child()
    }
}
