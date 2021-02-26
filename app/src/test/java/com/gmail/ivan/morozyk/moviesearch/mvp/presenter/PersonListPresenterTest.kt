package com.gmail.ivan.morozyk.moviesearch.mvp.presenter

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result
import com.gmail.ivan.morozyk.moviesearch.data.Person
import com.gmail.ivan.morozyk.moviesearch.data.PersonDto
import com.gmail.ivan.morozyk.moviesearch.data.SearchPersonRootDto
import com.gmail.ivan.morozyk.moviesearch.data.mapper.BaseMapper
import com.gmail.ivan.morozyk.moviesearch.data.mapper.HttpError
import com.gmail.ivan.morozyk.moviesearch.data.service.PersonService
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.`PersonListContract$View$$State`
import com.gmail.ivan.morozyk.moviesearch.navigation.Action
import com.gmail.ivan.morozyk.moviesearch.navigation.Router
import io.mockk.*
import io.mockk.junit5.MockKExtension
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith

class PersonListPresenterTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val personService: PersonService = mockk()

    private val router: Router = mockk(relaxed = true)

    private val view: `PersonListContract$View$$State` = mockk(relaxed = true)

    private val personMapper: BaseMapper<PersonDto, Person> = mockk()

    private val personDto: PersonDto = mockk()

    private val person: Person = mockk()

    private val personList = listOf(person)

    private val errorMapper: BaseMapper<Int, HttpError> = mockk()

    private val presenter: PersonListPresenter =
        PersonListPresenter(personService, personMapper, errorMapper, router)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
        clearAllMocks()
        presenter.setViewState(view)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search person proper query return list`() = runBlockingTest {
        val query = "Neil"

        coEvery { personService.getPersonList(query) } returns Result.success(
            SearchPersonRootDto(
                listOf(personDto)
            )
        )
        every {
            personMapper.map(personDto)
        } returns person

        //test section
        every { view.showError(HttpError.NoInternetError) } answers { println("show error") }
        every { view.showPersons(personList) } answers { println("show list") }
        every { view.showProgress() } answers { println("show progress") }
        every { view.hideProgress() } answers { println("hide progress") }
        every { view.showEmpty() } answers { println("show empty") }
        every { view.clearSearch() } answers { println("clear search") }



        presenter.searchPerson(query)

        verifyOrder {
            view.showProgress()
            view.showPersons(personList)
            view.hideProgress()
        }
    }

    @Test
    fun `search person nothing found shows empty`() = runBlockingTest {
        val query = "Neil"

        coEvery { personService.getPersonList(query) } returns Result.success(
            SearchPersonRootDto(
                emptyList()
            )
        )

        presenter.searchPerson(query)

        verifyOrder {
            view.showProgress()
            view.showEmpty()
            view.hideProgress()
        }
    }

    @Test
    fun `search person no internet shows error`() {
        val query = "Neil"

        coEvery { personService.getPersonList(query) } returns Result.error(FuelError.wrap(Exception()))
        every { errorMapper.map(-1) } returns HttpError.NoInternetError

        presenter.searchPerson(query)

        verifyOrder {
            view.showProgress()
            view.showError(HttpError.NoInternetError)
            view.hideProgress()
        }
    }

    @Test
    fun `clear search button clears search`() {
        presenter.clearSearchButtonClicked()
        verify {
            view.clearSearch()
        }
    }

    @Test
    fun `person clicked routes to person details`() {
        val personId = ""
        presenter.onPersonClicked(personId)
        verify { router.navigate(Action.FromPersonListToPersonDetails(personId)) }
    }

    @Test
    fun `refresh called without query`() {
        presenter.refresh()
        verify {
            view.clearSearch()
        }
    }

    @Test
    fun `refresh called with query`() {
        val query = "query"

        coEvery { personService.getPersonList(query) } returns Result.success(
            SearchPersonRootDto(
                listOf(personDto)
            )
        )
        every {
            personMapper.map(personDto)
        } returns person

        presenter.searchPerson(query)
        clearMocks(view)
        presenter.refresh()

        verifyOrder {
            view.showProgress()
            view.showPersons(personList)
            view.hideProgress()
        }
    }
}