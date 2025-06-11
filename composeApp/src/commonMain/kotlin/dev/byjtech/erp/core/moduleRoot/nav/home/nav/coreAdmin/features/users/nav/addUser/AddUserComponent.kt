package dev.byjtech.erp.core.moduleRoot.nav.home.nav.coreAdmin.features.users.nav.addUser

import dev.byjtech.erp.common.PermissionAwareComponent

interface AddUserComponent: PermissionAwareComponent {
    val onFinished: (added: Boolean) -> Unit
}