package com.gmail.ivan.morozyk.moviesearch.mvp.contract

import moxy.MvpView
import moxy.viewstate.strategy.SkipStrategy
import moxy.viewstate.strategy.StateStrategyType

interface SettingsContract {

    @StateStrategyType(SkipStrategy::class)
    interface View : MvpView {

        fun applyTheme(theme: Theme)

        fun showCurrentTheme(theme: Theme)
    }

    interface Presenter {

        fun getCurrentTheme()

        fun onSwitchThemeClicked(dark: Boolean)
    }
}

enum class Theme {
    LIGHT,
    DARK
}