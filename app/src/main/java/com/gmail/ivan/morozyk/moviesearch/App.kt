package com.gmail.ivan.morozyk.moviesearch

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.github.kittinunf.fuel.core.FuelManager
import com.gmail.ivan.morozyk.moviesearch.di.*
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.Theme
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(
            if (getSharedPreferences(App::class.qualifiedName, MODE_PRIVATE).getBoolean(
                    Theme::class.simpleName, false
                )
            ) MODE_NIGHT_YES else MODE_NIGHT_NO
        )

        FuelManager.instance.basePath = "https://imdb-api.com/en/API"

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                servicesModule,
                mappersModule,
                titleListModule,
                titleDetailsModule,
                personListModule,
                personDetailsModule,
                settingsModule
            )
        }
    }
}