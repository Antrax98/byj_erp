package dev.byjtech.erp.session

import dev.byjtech.erp.core.session.AppSession

class OtherPlatformSessionHandler : SessionHandler {
    override fun handleNewSession(session: AppSession) {
        // Handle the new session on desktop
        println("Other platform received new session: $session")
        // This function does nothing
    }
}