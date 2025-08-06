package dev.byjtech.erp.common.session

import io.ktor.client.plugins.cookies.*
import io.ktor.http.*
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.encodeToString
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import com.russhwolf.settings.Settings
import io.ktor.util.date.GMTDate

class SettingsCookieStorage(
    private val settings: Settings
) : CookiesStorage {

    private val mutex = Mutex()
    private val json = Json { ignoreUnknownKeys = true }
    private val cookieKeysKey = "cookie_keys"

    override suspend fun addCookie(requestUrl: Url, cookie: Cookie) {
        mutex.withLock {
            val key = cookieKey(cookie)
            val encoded = json.encodeToString(cookie)
            settings.putString(key, encoded)

            // Guardar clave en el índice si no existe
            val keys = getStoredKeys().toMutableSet()
            if (keys.add(key)) {
                println("Key added: $key")
                saveStoredKeys(keys)
            } else {
                println("Key already exists: $key")
            }
            println("Stored cookie keys: ${getStoredKeys()}")
        }
    }

    override suspend fun get(requestUrl: Url): List<Cookie> {
        return mutex.withLock {
            val keys = getStoredKeys()
            keys.mapNotNull { key ->
                val cookieString = settings.getStringOrNull(key)
                try {
                    cookieString?.let { json.decodeFromString<Cookie>(it) }
                } catch (e: Exception) {
                    null
                }
            }.filter { !it.isExpired() }
        }
    }

    override fun close() {
        // ....
    }

    suspend fun clearAll() {
        mutex.withLock {
            val keys = getStoredKeys()
            keys.forEach { settings.remove(it) }
            settings.remove(cookieKeysKey)
        }
    }

    private fun cookieKey(cookie: Cookie): String = "cookie_${cookie.name}_${cookie.domain}_${cookie.path}"

    private fun getStoredKeys(): Set<String> {
        return settings.getStringOrNull(cookieKeysKey)
            ?.let { json.decodeFromString(it) }
            ?: emptySet()
    }

    private fun saveStoredKeys(keys: Set<String>) {
        settings.putString(cookieKeysKey, json.encodeToString(keys))
    }

    private fun Cookie.isExpired(): Boolean {
        return expires != null && expires!!.timestamp < GMTDate().timestamp
    }
}