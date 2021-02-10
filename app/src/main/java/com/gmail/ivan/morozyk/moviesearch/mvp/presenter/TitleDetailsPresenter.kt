package com.gmail.ivan.morozyk.moviesearch.mvp.presenter

import com.github.kittinunf.fuel.core.FuelError
import com.gmail.ivan.morozyk.moviesearch.data.mapper.TitleDtoMapper
import com.gmail.ivan.morozyk.moviesearch.data.service.TitleService
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.TitleDetailsContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.MvpPresenter
import moxy.presenterScope

class TitleDetailsPresenter(
    private val titleService: TitleService,
    private val mapper: TitleDtoMapper
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
                showError(it)
            }).also { viewState.hideProgress() }
        }
    }

    private fun showError(error: FuelError) {
        if (error.response.statusCode == -1) {
            viewState.showInternetError()
        } else {
            viewState.showUnknownError()
        }
    }
}