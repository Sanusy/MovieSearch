package com.gmail.ivan.morozyk.moviesearch.mvp.presenter

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result
import com.gmail.ivan.morozyk.moviesearch.data.Title
import com.gmail.ivan.morozyk.moviesearch.data.TitleDto
import com.gmail.ivan.morozyk.moviesearch.data.mapper.BaseMapper
import com.gmail.ivan.morozyk.moviesearch.data.mapper.HttpError
import com.gmail.ivan.morozyk.moviesearch.data.service.TitleService
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.`TitleDetailsContract$View$$State`
import com.gmail.ivan.morozyk.moviesearch.navigation.Action
import com.gmail.ivan.morozyk.moviesearch.navigation.Router
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class TitleDetailsPresenterTest{
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val view: `TitleDetailsContract$View$$State` = mockk(relaxed = true)

    private val titleService: TitleService = mockk()

    private val titleMapper: BaseMapper<TitleDto, Title> = mockk()

    private val errorMapper: BaseMapper<Int, HttpError> = mockk()

    private val router: Router = mockk(relaxed = true)

    private val presenter = TitleDetailsPresenter(titleService, titleMapper, errorMapper, router)

    private val titleId = ""

    private val titleDto: TitleDto = mockk()

    private val title: Title = mockk()

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
        coEvery { titleService.getTitleById(titleId) } returns Result.success(titleDto)
        every { titleMapper.map(titleDto) } returns title

        presenter.loadTitle(titleId)

        verifyOrder {
            view.showProgress()
            view.showTitle(title)
            view.hideProgress()
        }
    }

    @Test
    fun `load person not loaded shows error`(){
        coEvery { titleService.getTitleById(titleId) } returns Result.error(FuelError.wrap(Exception()))
        every { errorMapper.map(-1) } returns HttpError.NoInternetError

        presenter.loadTitle(titleId)

        verifyOrder {
            view.showProgress()
            view.showError(HttpError.NoInternetError)
            view.hideProgress()
        }
    }

    @Test
    fun `title clicked opens title details`(){
        val personId = ""

        presenter.onPersonClicked(personId)

        verify{router.navigate(Action.FromPersonDetailsToTitleDetails(personId))}
    }
}