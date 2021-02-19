package com.gmail.ivan.morozyk.moviesearch

import android.content.Context
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.Theme

class SharedPrefHelper(private val context: Context) {

    fun getTheme() =
        Theme.valueOf(
            context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE)
                .getString(THEME, Theme.LIGHT.name)!!
        )

    fun putTheme(theme: Theme) {
        context.getSharedPreferences(STORAGE_NAME, Context.MODE_PRIVATE).edit()
            .putString(THEME, theme.name).apply()
    }

    companion object {
        private const val THEME = "current theme"

        private const val STORAGE_NAME = "MovieSearchSharedPrefs"
    }
}