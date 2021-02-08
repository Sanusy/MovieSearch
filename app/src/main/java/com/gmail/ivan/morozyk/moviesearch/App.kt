package com.gmail.ivan.morozyk.moviesearch

import android.app.Application
import com.github.kittinunf.fuel.core.FuelManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        FuelManager.instance.basePath = "https://imdb-api.com/en/API"
    }
}