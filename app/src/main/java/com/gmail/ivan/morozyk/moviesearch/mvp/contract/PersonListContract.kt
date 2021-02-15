package com.gmail.ivan.morozyk.moviesearch.mvp.contract

import com.gmail.ivan.morozyk.moviesearch.data.Person
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface PersonListContract {

    @StateStrategyType(AddToEndSingleStrategy::class)
    interface View : MvpView {

        fun showPersons(personList: List<Person>)

        fun showInternetError()

        fun showUnknownError()

        fun showEmpty()

        fun showProgress()

        fun hideProgress()

        fun clearSearch()
    }

    interface Presenter {

        fun refresh()

        fun searchPerson(query: String)

        fun clearSearchButtonClicked()
    }
}