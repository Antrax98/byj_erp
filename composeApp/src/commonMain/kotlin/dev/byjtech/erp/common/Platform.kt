package dev.byjtech.erp.common

expect fun getPlatform(): Platform

sealed class Platform {
    data object Android : Platform()
    data object Desktop : Platform()
    //para el futuro
    data object Ios : Platform()
    data object Web : Platform()
}