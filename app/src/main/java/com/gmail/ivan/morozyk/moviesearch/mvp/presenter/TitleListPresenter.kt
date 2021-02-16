package com.gmail.ivan.morozyk.moviesearch.mvp.presenter

import com.gmail.ivan.morozyk.moviesearch.data.Title
import com.gmail.ivan.morozyk.moviesearch.data.TitleDto
import com.gmail.ivan.morozyk.moviesearch.data.mapper.BaseMapper
import com.gmail.ivan.morozyk.moviesearch.data.service.HttpErrorMapper
import com.gmail.ivan.morozyk.moviesearch.data.service.TitleService
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.QueryType
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.TitleListContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.MvpPresenter
import moxy.presenterScope

class TitleListPresenter(
    private val titleService: TitleService,
    private val titleMapper: BaseMapper<TitleDto, Title>,
    private val errorMapper: HttpErrorMapper
) :
    MvpPresenter<TitleListContract.View>(), TitleListContract.Presenter {

    private var query: Query = Query.GetQuery(QueryType.TOP_250_MOVIES)

    override fun attachView(view: TitleListContract.View?) {
        super.attachView(view)
        viewState.showProgress()
        getTitleList(QueryType.TOP_250_MOVIES)
    }

    override fun searchTitleByWord(word: String) {
        viewState.showProgress()

        query = Query.SearchQuery(word)

        presenterScope.launch {
            val result = withContext(Dispatchers.IO) { titleService.searchTitlesByWord(word) }
            result.fold(
                success = {
                    val titlesToShow = it.results.map { titleDto ->
                        titleMapper.map(titleDto)
                    }

                    with(viewState) {
                        if (titlesToShow.isNullOrEmpty()) {
                            showEmpty()
                        } else {
                            showTitleList(titlesToShow)
                        }
                        hideProgress()
                    }
                },
                failure = {
                    viewState.showError(errorMapper.mapErrorCode(it.response.statusCode))
                })
        }
    }

    override fun getTitleList(queryType: QueryType) {
        viewState.showProgress()

        query = Query.GetQuery(queryType)

        presenterScope.launch {
            val result =
                withContext(Dispatchers.IO) { titleService.getTitlesByQuery(queryType.queryString) }
            result.fold(
                success = {
                    val titlesToShow = it.items.map { titleDto ->
                        titleMapper.map(titleDto)
                    }

                    with(viewState) {
                        if (titlesToShow.isNullOrEmpty()) {
                            showEmpty()
                        } else {
                            showTitleList(titlesToShow)
                        }
                        hideProgress()
                    }
                },
                failure = {
                    viewState.showError(errorMapper.mapErrorCode(it.response.statusCode))
                })
        }
    }

    override fun clearSearchButtonClicked() {
        viewState.clearSearch()
    }

    override fun refresh() = when (query) {
        is Query.GetQuery -> getTitleList((query as Query.GetQuery).type)
        is Query.SearchQuery -> searchTitleByWord((query as Query.SearchQuery).query)
    }

    private sealed class Query {
        data class GetQuery(val type: QueryType) : Query()
        data class SearchQuery(val query: String) : Query()
    }
}