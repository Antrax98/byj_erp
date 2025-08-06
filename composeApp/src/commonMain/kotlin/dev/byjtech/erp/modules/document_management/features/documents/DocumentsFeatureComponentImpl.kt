package dev.byjtech.erp.modules.document_management.features.documents

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.childContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.lifecycle.coroutines.coroutineScope
import dev.byjtech.erp.common.PermissionKey
import dev.byjtech.erp.common.api.ApiClient
import dev.byjtech.erp.modules.document_management.features.documents.nav.documentsMain.DocumentsMainComponent
import dev.byjtech.erp.modules.document_management.features.documents.nav.documentsMain.DocumentsMainComponentImpl
import dev.byjtech.erp.modules.document_management.features.documents.nav.documentsMain.DocumentsMainHomeComponent
import dev.byjtech.erp.modules.document_management.features.documents.nav.documentsMain.DocumentsMainHomeComponentImpl
import dev.byjtech.erp.modules.document_management.features.documents.nav.documentPage.DocumentPageComponent
import dev.byjtech.erp.modules.document_management.features.documents.nav.documentPage.DocumentPageComponentImpl
import dev.byjtech.erp.modules.document_management.features.documents.nav.addDocument.AddDocumentComponent
import dev.byjtech.erp.modules.document_management.features.documents.nav.addDocument.AddDocumentComponentImpl
import dev.byjtech.erp.modules.document_management.features.documents.nav.editDocument.EditDocumentComponent
import dev.byjtech.erp.modules.document_management.features.documents.nav.editDocument.EditDocumentComponentImpl
import dev.byjtech.erp.modules.document_management.features.documents.nav.documentHistory.DocumentHistoryComponent
import dev.byjtech.erp.modules.document_management.features.documents.nav.documentHistory.DocumentHistoryComponentImpl
import dev.byjtech.erp.modules.document_management.features.documents.nav.editHistory.EditHistoryComponent
import dev.byjtech.erp.modules.document_management.features.documents.nav.editHistory.EditHistoryComponentImpl
import dev.byjtech.erp.modules.document_management.features.documents.nav.auditLogs.AuditLogsComponent
import dev.byjtech.erp.modules.document_management.features.documents.nav.auditLogs.AuditLogsComponentImpl
import dev.byjtech.erp.modules.document_management.features.notifications.NotificationsViewModel
import dev.byjtech.erp.modules.document_management.features.notifications.NotificationSettingsViewModel
import dev.byjtech.erp.modules.document_management.features.notifications.NotificationsComponent
import dev.byjtech.erp.modules.document_management.features.notifications.NotificationsComponentImpl
import dev.byjtech.erp.modules.document_management.features.notifications.NotificationSettingsComponent
import dev.byjtech.erp.modules.document_management.features.notifications.NotificationSettingsComponentImpl
import dev.byjtech.erp.modules.document_management.api.DocumentManagementClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import com.arkivanov.decompose.DelicateDecomposeApi

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

    // DocumentManagementClient compartido
    private val documentManagementClient = DocumentManagementClient(apiClient.clientKtor)

    // Notification ViewModels compartidos
    private val notificationsViewModel = NotificationsViewModel(
        documentManagementClient,
        coroutineScope
    )
    private val notificationSettingsViewModel = NotificationSettingsViewModel(
        documentManagementClient,
        coroutineScope
    )

    init {
        // Cargar contador de notificaciones al inicio
        coroutineScope.launch {
            notificationsViewModel.loadNotificationCount()
        }
    }

    override fun onBack(): Boolean {
        // Implementación simple - siempre permite ir hacia atrás
        navigation.pop()
        return true
    }

    // Navegación
    @Serializable
    sealed class Config {
        @Serializable
        data object DocumentsHome : Config()
        @Serializable  
        data object DocumentsMain : Config()
        @Serializable
        data class DocumentPage(val documentId: String) : Config()
        @Serializable
        data object AddDocument : Config()
        @Serializable
        data class EditDocument(val documentId: String) : Config()
        @Serializable
        data class DocumentHistory(val documentId: String) : Config()
        @Serializable
        data object Notifications : Config()
        @Serializable
        data object NotificationSettings : Config()
        @Serializable
        data object EditHistory : Config()
        @Serializable
        data object AuditLogs : Config()
    }

    private val navigation = StackNavigation<Config>()

    private val stack = childStack(
        source = navigation,
        initialStack = { listOf(Config.DocumentsHome) },
        saveStack = { null },
        restoreStack = { null },
        handleBackButton = true,
        childFactory = ::childFactory
    )

    override val childStack: Value<ChildStack<Config, DocumentsFeatureComponent.Child>> = stack

    private fun childFactory(config: Config, componentContext: ComponentContext): DocumentsFeatureComponent.Child =
        when (config) {
            is Config.DocumentsHome -> DocumentsFeatureComponent.Child.DocumentsHome(
                DocumentsMainHomeComponentImpl(
                    navTo = ::navigateTo
                )
            )
            is Config.DocumentsMain -> DocumentsFeatureComponent.Child.DocumentsList(
                DocumentsMainComponentImpl(
                    componentContext = componentContext,
                    userPermissions = userPermissions,
                    apiClient = apiClient,
                    navTo = ::navigateTo,
                    notificationCount = notificationsViewModel.notificationCount
                )
            )
            is Config.DocumentPage -> DocumentsFeatureComponent.Child.DocumentPage(
                DocumentPageComponentImpl(
                    componentContext = componentContext,
                    userPermissions = userPermissions,
                    apiClient = apiClient,
                    navTo = ::navigateTo,
                    documentId = config.documentId,
                    onNavigateBackCallback = { navigation.pop() },
                    onDocumentDeactivatedCallback = { navigation.pop() }
                )
            )
            is Config.AddDocument -> DocumentsFeatureComponent.Child.AddDocument(
                AddDocumentComponentImpl(
                    componentContext = componentContext,
                    userPermissions = userPermissions,
                    apiClient = apiClient,
                    navTo = ::navigateTo,
                    onNavigateBack = { navigation.pop() }
                )
            )
            is Config.EditDocument -> DocumentsFeatureComponent.Child.EditDocument(
                EditDocumentComponentImpl(
                    componentContext = componentContext,
                    userPermissions = userPermissions,
                    apiClient = apiClient,
                    navTo = ::navigateTo,
                    documentId = config.documentId,
                    onNavigateBack = { navigation.pop() }
                )
            )
            is Config.DocumentHistory -> DocumentsFeatureComponent.Child.DocumentHistory(
                DocumentHistoryComponentImpl(
                    componentContext = componentContext,
                    userPermissions = userPermissions,
                    apiClient = apiClient,
                    documentId = config.documentId,
                    onNavigateBack = { navigation.pop() }
                )
            )
            is Config.Notifications -> DocumentsFeatureComponent.Child.Notifications(
                NotificationsComponentImpl(
                    componentContext = componentContext,
                    viewModel = notificationsViewModel,
                    onNavigateBackCallback = { navigation.pop() },
                    onNavigateToSettingsCallback = { navigateTo(Config.NotificationSettings) }
                )
            )
            is Config.NotificationSettings -> DocumentsFeatureComponent.Child.NotificationSettings(
                NotificationSettingsComponentImpl(
                    componentContext = componentContext,
                    viewModel = notificationSettingsViewModel,
                    onNavigateBackCallback = { navigation.pop() }
                )
            )
            is Config.EditHistory -> DocumentsFeatureComponent.Child.EditHistory(
                EditHistoryComponentImpl(
                    componentContext = componentContext,
                    userPermissions = userPermissions,
                    apiClient = documentManagementClient,
                    onNavigateBack = { navigation.pop() }
                )
            )
            is Config.AuditLogs -> DocumentsFeatureComponent.Child.AuditLogs(
                AuditLogsComponentImpl(
                    componentContext = componentContext,
                    userPermissions = userPermissions,
                    apiClient = documentManagementClient,
                    onNavigateBack = { navigation.pop() }
                )
            )
        }

    private fun navigateTo(target: Config) {
        navigation.push(target)
        changeTitle(target)
    }

    private fun changeTitle(config: Config) {
        val title = when (config) {
            Config.DocumentsHome -> "Gestión de Documentos"
            Config.DocumentsMain -> "Lista de Documentos"
            is Config.DocumentPage -> "Detalle de Documento"
            Config.AddDocument -> "Nuevo Documento"
            is Config.EditDocument -> "Editar Documento"
            is Config.DocumentHistory -> "Historial de Edición"
            Config.Notifications -> "Notificaciones"
            Config.NotificationSettings -> "Configuración de Notificaciones"
            Config.EditHistory -> "Historial de Ediciones"
            Config.AuditLogs -> "Logs de Auditoría"
        }
        updateTitle(title)
    }

    init {
        changeTitle(Config.DocumentsHome)
    }
}
