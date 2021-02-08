package com.gmail.ivan.morozyk.moviesearch.mvp.presenter

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

    override fun attachView(view: TitleListContract.View?) {
        super.attachView(view)
        viewState.showProgress()
        getTitleList(QueryType.MOST_POPULAR_MOVIES)
    }

    override fun searchTitleByWord(word: String) {
        viewState.showProgress()
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
                        setAppTitle(null)
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
                            setAppTitle(null)
                        } else {
                            showTitleList(titlesToShow)
                            setAppTitle(queryType)
                        }
                        hideProgress()
                    }
                },
                failure = {
                    showError(it)
                })
        }
    }

    private fun showError(error: Throwable) {
        if (error.message != null && error.message!!.contains("Unable to resolve host \"imdb-api.com\": No address associated with hostname")) {
            viewState.showInternetConnectionError()
        } else {
            viewState.showUnknownError()
        }
        viewState.hideProgress()
    }
}