package com.gmail.ivan.morozyk.moviesearch.mvp.contract

import com.gmail.ivan.morozyk.moviesearch.data.Title
import com.gmail.ivan.morozyk.moviesearch.data.mapper.HttpError
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface TitleDetailsContract {

    @StateStrategyType(AddToEndSingleStrategy::class)
    interface View : MvpView {

        fun showTitle(title: Title)

        fun showError(error: HttpError)

        fun showProgress()

        fun hideProgress()
    }

    interface Presenter {

        fun loadTitle(titleId: String)
    }
}