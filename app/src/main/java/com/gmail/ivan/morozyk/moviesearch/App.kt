package com.gmail.ivan.morozyk.moviesearch

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.github.kittinunf.fuel.core.FuelManager
import com.gmail.ivan.morozyk.moviesearch.di.*
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.Theme
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App : Application() {

    private val sharedPrefHelper: SharedPrefHelper by inject()

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@App)
            modules(
                navigationModule,
                localStorageModule,
                servicesModule,
                mappersModule,
                titleListModule,
                titleDetailsModule,
                personListModule,
                personDetailsModule,
                settingsModule,
                dataBaseModule,
            )
        }

        AppCompatDelegate.setDefaultNightMode(
            when (sharedPrefHelper.getTheme()) {
                Theme.LIGHT -> MODE_NIGHT_NO
                Theme.DARK -> MODE_NIGHT_YES
            }
        )

        FuelManager.instance.basePath = "https://imdb-api.com/en/API"
    }
}