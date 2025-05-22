package dev.byjtech.erp.common.session

import com.russhwolf.settings.PropertiesSettings
import com.russhwolf.settings.Settings
import java.io.File
import java.util.Properties

actual fun provideSettings(): Settings {
    val file = File(System.getProperty("user.home"), ".erp_app.preferences.properties")
    val properties = Properties().apply {
        if (file.exists()) {
            file.inputStream().use { load(it) }
        }
    }

    return PropertiesSettings(properties) {
        // Guarda automáticamente al hacer cambios
        file.outputStream().use { properties.store(it, null) }
    }
}
