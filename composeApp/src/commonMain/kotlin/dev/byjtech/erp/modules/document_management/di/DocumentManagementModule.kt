package dev.byjtech.erp.modules.document_management.di

import dev.byjtech.erp.common.FeatureEntry
import dev.byjtech.erp.common.ModuleEntry
import dev.byjtech.erp.document_management.DocumentManagementDefinition
import dev.byjtech.erp.modules.document_management.features.documents.DocumentsFeatureEntry
import dev.byjtech.erp.modules.document_management.features.auditLogs.AuditLogsFeatureEntry
import dev.byjtech.erp.modules.document_management.features.editHistory.EditHistoryFeatureEntry
import org.koin.dsl.module

val DocumentManagementModule = module {
    val featuresEntrySet = setOf<FeatureEntry>(
        DocumentsFeatureEntry,
        AuditLogsFeatureEntry,
        EditHistoryFeatureEntry,
    )

    single {
        ModuleEntry(
            name = DocumentManagementDefinition.name,
            features = featuresEntrySet
        )
    }
}
