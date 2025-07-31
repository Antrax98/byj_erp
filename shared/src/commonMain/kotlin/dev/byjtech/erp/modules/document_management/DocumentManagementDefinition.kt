package dev.byjtech.erp.document_management

import dev.byjtech.erp.common.*

public object DocumentManagementDefinition : ModuleDefinition {
    override val name = "document_management"
    override val displayName = "Gestión de Documentos"
    override val description = "Módulo de gestión de documentos, auditoría y edición"
    override val developerOnly = false

    object Documents : CategoryBase("documents", "Gestión de documentos", DocumentManagementDefinition) {
        val Create = permission("create", "Crear un nuevo documento")
        val View = permission("view", "Ver documentos")
        val Update = permission("update", "Actualizar un documento")
        val Delete = permission("delete", "Desactivar o anular un documento")
        val Disable = permission("disable", "Desactivar documento")
    }

    object AuditLogs : CategoryBase("audit_logs", "Bitácora de auditoría", DocumentManagementDefinition) {
        val View = permission("view", "Ver eventos de auditoría")
    }

    object EditHistory : CategoryBase("edit_history", "Historial de ediciones", DocumentManagementDefinition) {
        val View = permission("view", "Ver historial de ediciones")
    }

    object Notifications : CategoryBase("notifications", "Notificaciones", DocumentManagementDefinition) {
        val View = permission("view", "Ver notificaciones")
        val Configure = permission("configure", "Configurar notificaciones")
    }

    override val categories = setOf(Documents, AuditLogs, EditHistory, Notifications)
}
