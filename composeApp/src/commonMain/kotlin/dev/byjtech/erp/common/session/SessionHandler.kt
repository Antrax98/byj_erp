package dev.byjtech.erp.common.session

import dev.byjtech.erp.core.session.AppSession

interface SessionHandler {
    fun handleNewSession(session: AppSession)
}