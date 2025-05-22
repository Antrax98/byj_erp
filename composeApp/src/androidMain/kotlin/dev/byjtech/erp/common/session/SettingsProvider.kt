package dev.byjtech.erp.common.session

import android.content.Context
import com.russhwolf.settings.Settings
import com.russhwolf.settings.SharedPreferencesSettings

lateinit var appContext: Context

fun initAndroidSettings(context: Context) {
    appContext = context
}

actual fun provideSettings(): Settings {
    val sharedPrefs = appContext.getSharedPreferences("my_settings", Context.MODE_PRIVATE)
    return SharedPreferencesSettings(sharedPrefs)
}