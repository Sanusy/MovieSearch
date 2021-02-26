package com.gmail.ivan.morozyk.moviesearch.mvp.presenter

import com.gmail.ivan.morozyk.moviesearch.SharedPrefHelper
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.Theme
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.`SettingsContract$View$$State`
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class SettingsPresenterTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val sharedPrefHelper: SharedPrefHelper = mockk(relaxed = true)

    private val view: `SettingsContract$View$$State` = mockk(relaxed = true)

    private val presenter = SettingsPresenter(sharedPrefHelper)

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
    fun `get current theme shows it picked`() {
        every { sharedPrefHelper.getTheme() } returns Theme.LIGHT

        presenter.getCurrentTheme()

        verify {
            view.showCurrentTheme(Theme.LIGHT)
        }
    }

    @Test
    fun `switch theme stores it and appies`() {
        presenter.onSwitchThemeClicked(true)

        verify {
            sharedPrefHelper.putTheme(Theme.DARK)
            view.applyTheme(Theme.DARK)
        }
    }
}