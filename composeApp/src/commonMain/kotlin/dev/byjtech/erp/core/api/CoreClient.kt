package dev.byjtech.erp.core.api

import de.jensklingenberg.ktorfit.Ktorfit

class CoreClient(ktorfit: Ktorfit) {
    val tenant = ktorfit.createCoreApi() //recomendado
    val tenantaux = ktorfit.create<CoreApi>() //porsiacaso
}