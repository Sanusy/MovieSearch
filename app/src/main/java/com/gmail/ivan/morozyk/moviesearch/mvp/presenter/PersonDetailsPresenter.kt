package com.gmail.ivan.morozyk.moviesearch.mvp.presenter

import com.gmail.ivan.morozyk.moviesearch.data.Person
import com.gmail.ivan.morozyk.moviesearch.data.PersonDto
import com.gmail.ivan.morozyk.moviesearch.data.mapper.BaseMapper
import com.gmail.ivan.morozyk.moviesearch.data.mapper.HttpError
import com.gmail.ivan.morozyk.moviesearch.data.service.PersonService
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.PersonDetailsContract
import com.gmail.ivan.morozyk.moviesearch.navigation.Action
import com.gmail.ivan.morozyk.moviesearch.navigation.Router
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moxy.MvpPresenter
import moxy.presenterScope

class PersonDetailsPresenter(
    private val personService: PersonService,
    private val personMapper: BaseMapper<PersonDto, Person>,
    private val errorMapper: BaseMapper<Int, HttpError>,
    private val router: Router
) : MvpPresenter<PersonDetailsContract.View>(), PersonDetailsContract.Presenter {

    override fun loadPerson(personId: String) {
        viewState.showProgress()
        presenterScope.launch {
            val result = withContext(Dispatchers.IO) {
                personService.getPersonById(personId)
            }

            result.fold(success = {
                viewState.showPerson(personMapper.map(it))
            }, failure = {
                viewState.showError(errorMapper.map(it.response.statusCode))
            })
                .also { viewState.hideProgress() }
        }
    }

    override fun onTitleClicked(titleId: String) {
        router.navigate(Action.FromPersonDetailsToTitleDetails(titleId))
    }
}