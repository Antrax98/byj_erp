package dev.byjtech.erp.core.domain.model

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import java.util.UUID

//CompanyModule relacion entre empresa y modulo
data class Subscription (
    val id: UUID = UUID.randomUUID(),
    val company: Company,
    val module: Module,
    val isActive: Boolean = true,//el tenant admin puede manejar este
    val isAccessible: Boolean= true,// el SuperAdmin maneja este otro
    val createdAt: LocalDateTime? = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()),
    val updatedAt: LocalDateTime? = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    //agregar datos de pago o algo asi aqui
    )