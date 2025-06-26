package dev.byjtech.erp.modules.moduleTest.infrastructure.api

import dev.byjtech.erp.shared.routing.ModuleRoutesInstaller
import dev.byjtech.erp.shared.routing.RoutesInstaller

class TestRoutesInstaller(
    installers: Set<RoutesInstaller>
): ModuleRoutesInstaller(
    installers
)