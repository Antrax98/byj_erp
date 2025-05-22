package dev.byjtech.erp

import android.app.Application
import dev.byjtech.erp.di.initKoin
import org.koin.android.ext.koin.androidContext

class MyApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@MyApplication)
        }

    }
}