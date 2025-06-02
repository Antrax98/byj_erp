package dev.byjtech.erp.core.domain.model

import kotlinx.datetime.LocalDateTime

//CompanyModule relacion entre empresa y modulo
data class Subscription (
    val id: Int,
    val company: Company,
    val module: Module,
    val isActive: Boolean,//el tenant admin puede manejar este
    val isAccessible: Boolean,// el SuperAdmin maneja este otro
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val billing: Billing?
    //agregar datos de pago o algo asi aqui
    )