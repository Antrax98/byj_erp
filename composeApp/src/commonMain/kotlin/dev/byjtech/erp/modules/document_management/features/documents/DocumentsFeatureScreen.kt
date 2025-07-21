package dev.byjtech.erp.modules.document_management.features.documents

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.byjtech.erp.modules.document_management.features.documents.nav.documentsMain.DocumentsMainScreen
import dev.byjtech.erp.modules.document_management.features.documents.nav.documentPage.DocumentPageScreen
import dev.byjtech.erp.modules.document_management.features.documents.nav.addDocument.AddDocumentScreen
import dev.byjtech.erp.modules.document_management.features.documents.nav.editDocument.EditDocumentScreen

@Composable
fun DocumentsFeatureScreen(component: DocumentsFeatureComponent) {
    val childStack by component.childStack.subscribeAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        Children(
            stack = childStack,
            modifier = Modifier.fillMaxSize(),
            animation = stackAnimation(fade())
        ) {
            when (val child = it.instance) {
                is DocumentsFeatureComponent.Child.DocumentsMain -> DocumentsMainScreen(child.component)
                is DocumentsFeatureComponent.Child.DocumentPage -> DocumentPageScreen(child.component)
                is DocumentsFeatureComponent.Child.AddDocument -> AddDocumentScreen(child.component)
                is DocumentsFeatureComponent.Child.EditDocument -> EditDocumentScreen(child.component)
            }
        }
    }
}
