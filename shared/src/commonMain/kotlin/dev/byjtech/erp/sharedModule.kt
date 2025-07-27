package dev.byjtech.erp

import dev.byjtech.erp.core.CoreDefinition
import dev.byjtech.erp.document_management.DocumentManagementDefinition
import dev.byjtech.erp.modules.TestDefinition
import org.koin.dsl.module

val sharedModule = module {
    single<CoreDefinition>{ CoreDefinition }
    single<DocumentManagementDefinition> { DocumentManagementDefinition }
    single<TestDefinition>{ TestDefinition }
}