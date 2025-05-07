package dev.byjtech.erp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform