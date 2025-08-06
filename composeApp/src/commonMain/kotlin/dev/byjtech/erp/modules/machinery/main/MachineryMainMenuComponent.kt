package dev.byjtech.erp.modules.machinery.main

import dev.byjtech.erp.common.PermissionAwareComponent

interface MachineryMainMenuComponent : PermissionAwareComponent {
    fun onViewMachineriesClick()
    fun onViewInactiveMachineriesClick()
    fun onCreateMachineryClick()
}
