package ru.viksimurg.viknote.app

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import ru.viksimurg.viknote.di.appModules
import ru.viksimurg.viknote.di.resourceModules
import ru.viksimurg.viknote.di.viewModels

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@App)
            modules(listOf(appModules, viewModels, resourceModules))
        }
    }

}