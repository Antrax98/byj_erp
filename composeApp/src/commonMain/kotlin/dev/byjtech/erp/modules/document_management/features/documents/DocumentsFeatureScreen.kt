package dev.byjtech.erp.modules.document_management.features.documents

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.extensions.compose.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.stack.animation.stackAnimation
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import dev.byjtech.erp.modules.document_management.features.documents.nav.documentsMain.DocumentsMainScreen

@OptIn(ExperimentalMaterial3Api::class)
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

@Composable
fun DocumentPageScreen(component: Any) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Página de Documento - ID: ${(component as dev.byjtech.erp.modules.document_management.features.documents.nav.documentPage.DocumentPageComponent).documentId}")
    }
}

@Composable
fun AddDocumentScreen(component: Any) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Agregar Documento")
    }
}

@Composable
fun EditDocumentScreen(component: Any) {
    Box(modifier = Modifier.fillMaxSize()) {
        Text("Editar Documento - ID: ${(component as dev.byjtech.erp.modules.document_management.features.documents.nav.editDocument.EditDocumentComponent).documentId}")
    }
}
