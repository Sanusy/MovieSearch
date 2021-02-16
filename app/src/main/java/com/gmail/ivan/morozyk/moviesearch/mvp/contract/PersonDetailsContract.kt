package com.gmail.ivan.morozyk.moviesearch.mvp.contract

import com.gmail.ivan.morozyk.moviesearch.data.Person
import com.gmail.ivan.morozyk.moviesearch.data.service.HttpError
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface PersonDetailsContract {

    @StateStrategyType(AddToEndSingleStrategy::class)
    interface View : MvpView {

        fun showPerson(person: Person)

        fun showError(error: HttpError)

        fun showProgress()

        fun hideProgress()
    }

    interface Presenter {

        fun loadPerson(personId: String)
    }
}