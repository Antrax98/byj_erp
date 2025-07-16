package dev.byjtech.erp.document_management.features.documents

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.document_management.features.documents.nav.documentsMain.DocumentsMainComponent
import dev.byjtech.erp.document_management.features.documents.nav.documentsMain.DocumentsMainComponentImpl
import dev.byjtech.erp.document_management.features.documents.nav.documentPage.DocumentPageComponent
import dev.byjtech.erp.document_management.features.documents.nav.documentPage.DocumentPageComponentImpl
import dev.byjtech.erp.document_management.features.documents.nav.addDocument.AddDocumentComponent
import dev.byjtech.erp.document_management.features.documents.nav.addDocument.AddDocumentComponentImpl
import dev.byjtech.erp.document_management.features.documents.nav.editDocument.EditDocumentComponent
import dev.byjtech.erp.document_management.features.documents.nav.editDocument.EditDocumentComponentImpl
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.Serializable

class DocumentsFeatureComponentImpl(
    val componentContext: ComponentContext,
    override val userPermissions: StateFlow<Set<PermissionKey>>,
    override val apiClient: ApiClient,
    override val toHome: () -> Unit,
    override val updateTitle: (newTitle: String) -> Unit
) : DocumentsFeatureComponent, ComponentContext by componentContext {

    private val coroutineScope = componentContext.coroutineScope()

    private val _state = MutableStateFlow(DocumentsFeatureState())
    override val state: StateFlow<DocumentsFeatureState> = _state

    override fun onBack(): Boolean {
        if (childStack.active.configuration == Config.DocumentsMain) {
            return false
        } else {
            navigation.pop {
                val newConfig = childStack.active.configuration
                changeTitle(newConfig)
            }
            return true
        }
    }

    // Navegación
    @Serializable
    sealed class Config {
        @Serializable
        data object DocumentsMain : Config()
        @Serializable
        data class DocumentPage(val documentId: String) : Config()
        @Serializable
        data object AddDocument : Config()
        @Serializable
        data class EditDocument(val documentId: String) : Config()
    }

    private val navigation = StackNavigation<Config>()

    private val stack = childStack(
        source = navigation,
        serializer = Config.serializer(),
        initialStack = { listOf(Config.DocumentsMain) },
        handleBackButton = true,
        childFactory = ::childFactory
    )

    override val childStack: Value<ChildStack<Config, DocumentsFeatureComponent.Child>> = stack

    private fun childFactory(config: Config, componentContext: ComponentContext): DocumentsFeatureComponent.Child =
        when (config) {
            is Config.DocumentsMain -> DocumentsFeatureComponent.Child.DocumentsMain(
                DocumentsMainComponentImpl(
                    componentContext = componentContext,
                    userPermissions = userPermissions,
                    apiClient = apiClient,
                    navTo = ::navigateTo
                )
            )
            is Config.DocumentPage -> DocumentsFeatureComponent.Child.DocumentPage(
                DocumentPageComponentImpl(
                    componentContext = componentContext,
                    userPermissions = userPermissions,
                    apiClient = apiClient,
                    navTo = ::navigateTo,
                    documentId = config.documentId
                )
            )
            is Config.AddDocument -> DocumentsFeatureComponent.Child.AddDocument(
                AddDocumentComponentImpl(
                    componentContext = componentContext,
                    userPermissions = userPermissions,
                    apiClient = apiClient,
                    navTo = ::navigateTo
                )
            )
            is Config.EditDocument -> DocumentsFeatureComponent.Child.EditDocument(
                EditDocumentComponentImpl(
                    componentContext = componentContext,
                    userPermissions = userPermissions,
                    apiClient = apiClient,
                    navTo = ::navigateTo,
                    documentId = config.documentId
                )
            )
        }

    private fun navigateTo(target: Config) {
        val current = childStack.value.active.configuration
        if (current != target) {
            navigation.replaceCurrent(target)
            changeTitle(target)
        }
    }

    private fun changeTitle(config: Config) {
        val title = when (config) {
            Config.DocumentsMain -> "Documentos"
            is Config.DocumentPage -> "Detalle de Documento"
            Config.AddDocument -> "Nuevo Documento"
            is Config.EditDocument -> "Editar Documento"
        }
        updateTitle(title)
    }

    init {
        changeTitle(Config.DocumentsMain)
    }
}
