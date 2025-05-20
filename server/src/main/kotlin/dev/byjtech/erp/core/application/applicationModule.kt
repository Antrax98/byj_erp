package dev.byjtech.erp.core.application

import dev.byjtech.erp.core.application.service.UserService
import org.koin.dsl.module

//TODO: aqui van los service a nivel de aplicacion, y hay que darles los repository correspondientes y services del domain, junto a sus implementaciones
//authServiceContract/Impl no va aqui por que el contrato esta fuera del modulo y se considera una dependencia externa (esos van en infrastructure)
val applicationModule = module {
    //TODO()
//    single<CompanyService> { CompanyService(get()) }
    //single<SessionService> { SessionService(get()) }
    single<UserService> { UserService(get()) }

}