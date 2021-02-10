package com.gmail.ivan.morozyk.moviesearch.mvp.contract

import com.gmail.ivan.morozyk.moviesearch.data.Title
import moxy.MvpView
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.StateStrategyType

interface TitleListContract {

    @StateStrategyType(AddToEndSingleStrategy::class)
    interface View : MvpView {

        fun showInternetConnectionError()

        fun showEmptyContentError()

        fun showUnknownError()

        fun showTitleList(titleList: List<Title>)

        fun clearSearch()

        fun showProgress()

        fun hideProgress()
    }

    interface Presenter {

        fun searchTitleByWord(word: String)

        fun getTitleList(queryType: QueryType)

        fun clearSearchButtonClicked()

        fun refresh()
    }
}

enum class QueryType(val queryString: String) {
    TOP_250_MOVIES("Top250Movies"),
    TOP_250_SERIES("Top250TVs"),
    MOST_POPULAR_MOVIES("MostPopularMovies"),
    MOST_POPULAR_SERIES("MostPopularTVs"),
    IN_THEATRES("InTheaters"),
    COMING_SOON("ComingSoon")
}