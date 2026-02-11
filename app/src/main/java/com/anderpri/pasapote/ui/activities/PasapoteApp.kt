package com.anderpri.pasapote.ui.activities

import android.app.Application
import com.anderpri.pasapote.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class PasapoteApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@PasapoteApp)
            modules(appModule)
        }
    }
}
