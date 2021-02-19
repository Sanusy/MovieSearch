package com.gmail.ivan.morozyk.moviesearch.mvp.presenter

import com.gmail.ivan.morozyk.moviesearch.SharedPrefHelper
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.SettingsContract
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.Theme
import moxy.MvpPresenter

class SettingsPresenter(private val sharedPrefHelper: SharedPrefHelper) :
    MvpPresenter<SettingsContract.View>(), SettingsContract.Presenter {

    override fun getCurrentTheme() {
        viewState.showCurrentTheme(sharedPrefHelper.getTheme())
    }

    override fun onSwitchThemeClicked(dark: Boolean) {
        val theme = if (dark) Theme.DARK else Theme.LIGHT
        sharedPrefHelper.putTheme(theme)
        viewState.applyTheme(theme)
    }
}