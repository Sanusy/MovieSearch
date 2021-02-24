package com.gmail.ivan.morozyk.moviesearch.mvp.presenter

import com.gmail.ivan.morozyk.moviesearch.data.Person
import com.gmail.ivan.morozyk.moviesearch.data.PersonDto
import com.gmail.ivan.morozyk.moviesearch.data.mapper.BaseMapper
import com.gmail.ivan.morozyk.moviesearch.data.mapper.HttpError
import com.gmail.ivan.morozyk.moviesearch.data.service.PersonService
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.PersonListContract
import com.gmail.ivan.morozyk.moviesearch.navigation.Action
import com.gmail.ivan.morozyk.moviesearch.navigation.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.MvpPresenter
import moxy.presenterScope

class PersonListPresenter(
    private val personService: PersonService,
    private val personMapper: BaseMapper<PersonDto, Person>,
    private val errorMapper: BaseMapper<Int, HttpError>,
    private val router: Router
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
                viewState.showError(errorMapper.map(it.response.statusCode))
            }).also { viewState.hideProgress() }
        }

    }

    override fun clearSearchButtonClicked() {
        viewState.clearSearch()
    }

    override fun onPersonClicked(personId: String) {
        router.navigate(Action.FromPersonListToPersonDetails(personId))
    }
}