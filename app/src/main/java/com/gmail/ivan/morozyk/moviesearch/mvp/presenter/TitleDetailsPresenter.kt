package com.gmail.ivan.morozyk.moviesearch.mvp.presenter

import com.gmail.ivan.morozyk.moviesearch.data.Title
import com.gmail.ivan.morozyk.moviesearch.data.TitleDto
import com.gmail.ivan.morozyk.moviesearch.data.mapper.BaseMapper
import com.gmail.ivan.morozyk.moviesearch.data.mapper.HttpError
import com.gmail.ivan.morozyk.moviesearch.data.service.TitleService
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.TitleDetailsContract
import com.gmail.ivan.morozyk.moviesearch.navigation.Action
import com.gmail.ivan.morozyk.moviesearch.navigation.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.MvpPresenter
import moxy.presenterScope

class TitleDetailsPresenter(
    private val titleService: TitleService,
    private val mapper: BaseMapper<TitleDto, Title>,
    private val errorMapper: BaseMapper<Int, HttpError>,
    private val router: Router
) : MvpPresenter<TitleDetailsContract.View>(),
    TitleDetailsContract.Presenter {

    override fun loadTitle(titleId: String) {
        viewState.showProgress()
        presenterScope.launch {
            val result = withContext(Dispatchers.IO) {
                titleService.getTitleById(titleId)
            }

            result.fold(success = {
                viewState.showTitle(mapper.map(it))
            }, failure = {
                viewState.showError(errorMapper.map(it.response.statusCode))
            }).also {
                viewState.hideProgress()
            }
        }
    }

    override fun onPersonClicked(personId: String) {
        router.navigate(Action.FromTitleDetailsToPersonDetails(personId))
    }
}