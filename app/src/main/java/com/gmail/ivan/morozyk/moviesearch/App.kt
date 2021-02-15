package com.gmail.ivan.morozyk.moviesearch

import android.app.Application
import com.github.kittinunf.fuel.core.FuelManager
import com.gmail.ivan.morozyk.moviesearch.di.mappersModule
import com.gmail.ivan.morozyk.moviesearch.di.presentersModule
import com.gmail.ivan.morozyk.moviesearch.di.servicesModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        FuelManager.instance.basePath = "https://imdb-api.com/en/API"

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(servicesModule, mappersModule, presentersModule)
        }
    }
}