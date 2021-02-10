package com.gmail.ivan.morozyk.moviesearch.mvp.contract

import com.gmail.ivan.morozyk.moviesearch.data.Title
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface TitleDetailsContract {

    @StateStrategyType(AddToEndSingleStrategy::class)
    interface View : MvpView {

        fun showTitle(title: Title)

        fun showInternetError()

        fun showUnknownError()

        fun showProgress()

        fun hideProgress()
    }

    interface Presenter {

        fun loadTitle(titleId: String)
    }
}