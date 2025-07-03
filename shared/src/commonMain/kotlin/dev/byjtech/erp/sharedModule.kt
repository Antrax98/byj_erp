package dev.byjtech.erp

import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.modules.document_management.DocumentManagementDefinition
import org.koin.dsl.module

val sharedModule = module {
    single<CoreDefinition>{ CoreDefinition }
    single<DocumentManagementDefinition> { DocumentManagementDefinition }
}