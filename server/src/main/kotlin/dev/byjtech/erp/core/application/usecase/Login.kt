package dev.byjtech.erp.core.application.usecase

import dev.byjtech.erp.core.domain.repository.SessionRepository
import dev.byjtech.erp.core.domain.repository.UserRepository

//TODO: ¿modificarlo para que revise si la empresa esta activa?

//esto se refiere mas bien a lo necesario para la ruta callback, ya que el login lo maneja google
class Login (
    private val userRepo: UserRepository,
    private val sessionRepo: SessionRepository
){
    fun exec (){

    }
}