package com.gmail.ivan.morozyk.moviesearch.mvp.presenter

import com.github.kittinunf.fuel.core.FuelError
import com.gmail.ivan.morozyk.moviesearch.data.mapper.TitleDtoMapper
import com.gmail.ivan.morozyk.moviesearch.data.service.TitleService
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.QueryType
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.TitleListContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.MvpPresenter
import moxy.presenterScope

class TitleListPresenter(private val titleService: TitleService) :
    MvpPresenter<TitleListContract.View>(), TitleListContract.Presenter {

    private var lastSearchQuery: String? = null

    private var lastGetQuery: QueryType? = null

    override fun attachView(view: TitleListContract.View?) {
        super.attachView(view)
        viewState.showProgress()
        getTitleList(QueryType.MOST_POPULAR_MOVIES)
    }

    override fun searchTitleByWord(word: String) {
        viewState.showProgress()

        lastSearchQuery = word
        lastGetQuery = null

        presenterScope.launch {
            val result = withContext(Dispatchers.IO) { titleService.searchTitlesByWord(word) }
            result.fold(
                success = {
                    val mapper = TitleDtoMapper()
                    val titlesToShow = it.results.map { titleDto ->
                        mapper.map(titleDto)
                    }

                    with(viewState) {
                        if (titlesToShow.isNullOrEmpty()) {
                            showEmptyContentError()
                        } else {
                            showTitleList(titlesToShow)
                        }
                        hideProgress()
                    }
                },
                failure = {
                    showError(it)
                })
        }
    }

    override fun getTitleList(queryType: QueryType) {
        viewState.showProgress()

        lastGetQuery = queryType
        lastSearchQuery = null

        presenterScope.launch {
            val result =
                withContext(Dispatchers.IO) { titleService.getTitlesByQuery(queryType.queryString) }
            result.fold(
                success = {
                    val mapper = TitleDtoMapper()
                    val titlesToShow = it.items.map { titleDto ->
                        mapper.map(titleDto)
                    }

                    with(viewState) {
                        if (titlesToShow.isNullOrEmpty()) {
                            showEmptyContentError()
                        } else {
                            showTitleList(titlesToShow)
                        }
                        hideProgress()
                    }
                },
                failure = {
                    showError(it)
                })
        }
    }

    override fun clearSearchButtonClicked() {
        viewState.clearSearch()
    }

    override fun refresh() {
        if (lastGetQuery != null) {
            getTitleList(lastGetQuery!!)
        } else if (lastSearchQuery != null) {
            searchTitleByWord(lastSearchQuery!!)
        }
    }

    private fun showError(error: FuelError) {
        if (error.response.statusCode == -1) {
            viewState.showInternetConnectionError()
        } else {
            viewState.showUnknownError()
        }
        viewState.hideProgress()
    }
}