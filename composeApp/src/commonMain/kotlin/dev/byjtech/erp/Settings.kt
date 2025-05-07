package dev.byjtech.erp

import com.russhwolf.settings.*
import java.util.UUID

fun getOrCreateDeviceId(settings: Settings): String {
    val key = "device_id"
    return settings.getStringOrNull(key) ?: UUID.randomUUID().toString().also {
        settings.putString(key, it)
    }
}