package dev.byjtech.erp.document_management.domain.model


import dev.byjtech.erp.common.CategoryBase
import dev.byjtech.erp.common.ModuleDefinition

object DocumentManagementDefinition : ModuleDefinition {
    override val name = "document_management"
    override val displayName = "Document Management"
    override val description = "Module for managing documents and files"
    override val developerOnly = false

    object Documents : CategoryBase("documents", "Document management", DocumentManagementDefinition) {
        val Create = permission("create", "Create a new document")
        val View = permission("view", "View document details")
        val Update = permission("update", "Update document information")
        val Delete = permission("delete", "Delete a document")
        val Upload = permission("upload", "Upload document files")
        val Download = permission("download", "Download document files")
    }

    object Audit : CategoryBase("audit", "Audit operations", DocumentManagementDefinition) {
        val View = permission("view", "View audit logs")
        val Export = permission("export", "Export audit logs")
    }

    override val categories = setOf(Documents, Audit)
}
