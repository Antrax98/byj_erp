package dev.byjtech.erp.machinery.infrastructure.api

import dev.byjtech.erp.shared.routing.ModuleRoutesInstaller
import dev.byjtech.erp.shared.routing.RoutesInstaller


class MachineryRoutesInstaller(
    installers: Set<RoutesInstaller>
) : ModuleRoutesInstaller(
    installers
)