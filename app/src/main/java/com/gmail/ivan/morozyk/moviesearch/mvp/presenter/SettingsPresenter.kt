package com.gmail.ivan.morozyk.moviesearch.mvp.presenter

import com.gmail.ivan.morozyk.moviesearch.mvp.contract.SettingsContract
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.Theme
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.ThemeStorage
import moxy.MvpPresenter

class SettingsPresenter(private val themeStorage: ThemeStorage) :
    MvpPresenter<SettingsContract.View>(), SettingsContract.Presenter {

    override fun onSwitchThemeClicked(dark: Boolean) {
        val theme = if (dark) Theme.DARK else Theme.LIGHT
        themeStorage.storeTheme(
            when (theme) {
                Theme.LIGHT -> false
                Theme.DARK -> true
            }
        )
        viewState.applyTheme(theme)
    }
}