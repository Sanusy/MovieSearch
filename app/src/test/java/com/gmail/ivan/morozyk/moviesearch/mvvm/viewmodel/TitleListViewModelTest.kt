package com.gmail.ivan.morozyk.moviesearch.mvvm.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.github.kittinunf.result.Result
import com.gmail.ivan.morozyk.moviesearch.data.SearchTitleRootDto
import com.gmail.ivan.morozyk.moviesearch.data.Title
import com.gmail.ivan.morozyk.moviesearch.data.TitleDto
import com.gmail.ivan.morozyk.moviesearch.data.mapper.BaseMapper
import com.gmail.ivan.morozyk.moviesearch.data.service.TitleService
import com.gmail.ivan.morozyk.moviesearch.mvvm.Resource
import com.gmail.ivan.morozyk.moviesearch.navigation.Router
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

class TitleListViewModelTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val titleService: TitleService = mockk()

    private val titleMapper: BaseMapper<TitleDto, Title> = mockk()

    private val errorMapper: BaseMapper<Int, Resource.HttpError<List<Title>>> = mockk()

    private val router: Router = mockk()

    private val viewModel = TitleListViewModel(titleService, titleMapper, errorMapper, router)

    private val observer: Observer<Resource<List<Title>>> =
        spyk(object : Observer<Resource<List<Title>>> {
            override fun onChanged(t: Resource<List<Title>>?) {
                print("${t!!::class.simpleName}")
            }
        })

    private val titleDto: TitleDto = mockk()

    private val title: Title = mockk()

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `search By Word Test proper Query returns List`() {
        val query = ""

        coEvery { titleService.searchTitlesByWord(query) } returns Result.success(
            SearchTitleRootDto(
                listOf(titleDto)
            )
        )

        every { titleMapper.map(titleDto) } returns title

        try {
            viewModel.titledData.observeForever(observer)

            viewModel.searchTitleByWord(query)

            verify {
                observer.onChanged(Resource.Loading())
                observer.onChanged(Resource.Success(listOf(title)))
            }
        } finally {
            viewModel.titledData.removeObserver(observer)
        }
    }
}