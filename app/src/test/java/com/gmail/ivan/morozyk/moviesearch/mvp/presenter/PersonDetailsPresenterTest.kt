package com.gmail.ivan.morozyk.moviesearch.mvp.presenter

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result
import com.gmail.ivan.morozyk.moviesearch.data.Person
import com.gmail.ivan.morozyk.moviesearch.data.PersonDto
import com.gmail.ivan.morozyk.moviesearch.data.mapper.BaseMapper
import com.gmail.ivan.morozyk.moviesearch.data.mapper.FuelHttpErrorMapper
import com.gmail.ivan.morozyk.moviesearch.data.mapper.HttpError
import com.gmail.ivan.morozyk.moviesearch.data.service.PersonService
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.`PersonDetailsContract$View$$State`
import com.gmail.ivan.morozyk.moviesearch.navigation.Action
import com.gmail.ivan.morozyk.moviesearch.navigation.Router
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class PersonDetailsPresenterTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val view: `PersonDetailsContract$View$$State` = mockk(relaxed = true)

    private val personService: PersonService = mockk()

    private val personMapper: BaseMapper<PersonDto, Person> = mockk()

    private val errorMapper: BaseMapper<Int, HttpError> = mockk()

    private val router: Router = mockk(relaxed = true)

    private val presenter = PersonDetailsPresenter(personService, personMapper, errorMapper, router)

    private val personId = ""

    private val personDto: PersonDto = mockk()

    private val person: Person = mockk()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        presenter.setViewState(view)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `load person loaded correctly shows person details`(){
        coEvery { personService.getPersonById(personId) } returns Result.success(personDto)
        every { personMapper.map(personDto) } returns person

        presenter.loadPerson(personId)

        verifyOrder {
            view.showProgress()
            view.showPerson(person)
            view.hideProgress()
        }
    }

    @Test
    fun `load person not loaded shows error`(){
        coEvery { personService.getPersonById(personId) } returns Result.error(FuelError.wrap(Exception()))
        every { errorMapper.map(-1) } returns HttpError.NoInternetError

        presenter.loadPerson(personId)

        verifyOrder {
            view.showProgress()
            view.showError(HttpError.NoInternetError)
            view.hideProgress()
        }
    }

    @Test
    fun `title clicked opens title details`(){
        val titleId = ""

        presenter.onTitleClicked(titleId)

        verify{router.navigate(Action.FromPersonDetailsToTitleDetails(titleId))}
    }
}