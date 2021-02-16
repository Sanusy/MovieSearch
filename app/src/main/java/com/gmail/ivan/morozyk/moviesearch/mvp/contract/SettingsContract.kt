package com.gmail.ivan.morozyk.moviesearch.mvp.contract

import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface SettingsContract {

    @StateStrategyType(AddToEndSingleStrategy::class)
    interface View : MvpView {

        fun applyTheme(theme: Theme)
    }

    interface Presenter {

        fun onSwitchThemeClicked(dark: Boolean)
    }
}

interface ThemeStorage {

    fun storeTheme(dark: Boolean)
}

enum class Theme {
    LIGHT,
    DARK
}