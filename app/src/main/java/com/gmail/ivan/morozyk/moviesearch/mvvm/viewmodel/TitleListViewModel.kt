package com.gmail.ivan.morozyk.moviesearch.mvvm.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gmail.ivan.morozyk.moviesearch.data.Title
import com.gmail.ivan.morozyk.moviesearch.data.TitleDto
import com.gmail.ivan.morozyk.moviesearch.data.mapper.BaseMapper
import com.gmail.ivan.morozyk.moviesearch.data.service.TitleService
import com.gmail.ivan.morozyk.moviesearch.mvvm.Resource
import com.gmail.ivan.morozyk.moviesearch.navigation.Action
import com.gmail.ivan.morozyk.moviesearch.navigation.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TitleListViewModel(
    private val titleService: TitleService,
    private val titleMapper: BaseMapper<TitleDto, Title>,
    private val errorMapper: BaseMapper<Int, Resource.HttpError<List<Title>>>,
    private val router: Router
) : ViewModel() {

    private val _titleData = MutableLiveData<Resource<List<Title>>>()
    val titledData = _titleData as LiveData<Resource<List<Title>>>

    private var query: Query = Query.GetQuery(QueryType.TOP_250_MOVIES)

    init {
//        getTitleList((query as Query.GetQuery).type)
    }

    fun searchTitleByWord(word: String) {
        _titleData.value = Resource.Loading()

        query = Query.SearchQuery(word)

        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) { titleService.searchTitlesByWord(word) }
            result.fold(
                success = {
                    val titlesToShow = it.results.map { titleDto ->
                        titleMapper.map(titleDto)
                    }

                    _titleData.value = Resource.Success(titlesToShow)
                },
                failure = {
                    _titleData.value = errorMapper.map(it.response.statusCode)
                })
        }
    }

    fun getTitleList(queryType: QueryType) {
        _titleData.value = Resource.Loading()

        query = Query.GetQuery(queryType)

        viewModelScope.launch {
            val result =
                withContext(Dispatchers.IO) { titleService.getTitlesByQuery(queryType.queryString) }
            result.fold(
                success = {
                    val titlesToShow = it.items.map { titleDto ->
                        titleMapper.map(titleDto)
                    }

                    _titleData.value = Resource.Success(titlesToShow)
                },
                failure = {
                    _titleData.value = errorMapper.map(it.response.statusCode)
                })
        }
    }

    fun refresh() {
        when (query) {
            is Query.GetQuery -> getTitleList((query as Query.GetQuery).type)
            is Query.SearchQuery -> searchTitleByWord((query as Query.SearchQuery).query)
        }
    }

    fun onTitleClicked(titleId: String) {
        router.navigate(Action.FromTitleListToTitleDetails(titleId))
    }

    private sealed class Query {
        data class GetQuery(val type: QueryType) : Query()
        data class SearchQuery(val query: String) : Query()
    }
}

enum class QueryType(val queryString: String) {
    TOP_250_MOVIES("Top250Movies"),
    TOP_250_SERIES("Top250TVs"),
    MOST_POPULAR_MOVIES("MostPopularMovies"),
    MOST_POPULAR_SERIES("MostPopularTVs"),
    COMING_SOON("ComingSoon"),
    IN_THEATRES("InTheaters")
}