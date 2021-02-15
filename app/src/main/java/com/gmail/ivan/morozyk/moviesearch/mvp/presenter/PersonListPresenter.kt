package com.gmail.ivan.morozyk.moviesearch.mvp.presenter

import com.github.kittinunf.fuel.core.FuelError
import com.gmail.ivan.morozyk.moviesearch.data.Person
import com.gmail.ivan.morozyk.moviesearch.data.PersonDto
import com.gmail.ivan.morozyk.moviesearch.data.mapper.BaseMapper
import com.gmail.ivan.morozyk.moviesearch.data.service.PersonService
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.PersonListContract
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.MvpPresenter
import moxy.presenterScope

class PersonListPresenter(
    private val personService: PersonService,
    private val personMapper: BaseMapper<PersonDto, Person>
) : MvpPresenter<PersonListContract.View>(), PersonListContract.Presenter {

    private var query: String = ""

    override fun refresh() {
        if (query.isEmpty()) {
            viewState.clearSearch()
        } else {
            searchPerson(query)
        }
    }

    override fun searchPerson(query: String) {
        this.query = query
        viewState.showProgress()
        presenterScope.launch {
            val result = withContext(Dispatchers.IO) {
                personService.getPersonList(query)
            }

            result.fold({
                if (it.results.isNullOrEmpty()) {
                    viewState.showEmpty()
                } else {
                    viewState.showPersons(it.results.map { personDto ->
                        personMapper.map(personDto)
                    })
                }
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

    override fun clearSearchButtonClicked() {
        viewState.clearSearch()
    }
}