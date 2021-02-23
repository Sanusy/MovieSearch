package com.gmail.ivan.morozyk.moviesearch.ui.fragment

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.gmail.ivan.morozyk.moviesearch.App
import com.gmail.ivan.morozyk.moviesearch.databinding.FragmentSettingsBinding
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.SettingsContract
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.Theme
import com.gmail.ivan.morozyk.moviesearch.mvp.presenter.SettingsPresenter
import com.gmail.ivan.morozyk.moviesearch.ui.activity.ItemSelected
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.get

class SettingsFragment : BaseFragment<FragmentSettingsBinding>(), SettingsContract.View {

    @InjectPresenter
    lateinit var presenter: SettingsPresenter

    @ProvidePresenter
    fun providePresenter(): SettingsPresenter = get()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentSettingsBinding.inflate(inflater, container, attachToRoot)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.settingsThemeSwitch.isChecked =
            requireContext().getSharedPreferences(App::class.qualifiedName, MODE_PRIVATE)
                .getBoolean(Theme::class.simpleName, false)
        binding.settingsThemeSwitch.setOnCheckedChangeListener { _, isChecked ->
            presenter.onSwitchThemeClicked(
                isChecked
            )
        }
    }

    override fun onStart() {
        super.onStart()

        requireMainActivity().setSelectedItem(ItemSelected.SETTINGS)
        presenter.getCurrentTheme()
    }

    override fun applyTheme(theme: Theme) {
        AppCompatDelegate.setDefaultNightMode(
            when (theme) {
                Theme.LIGHT -> AppCompatDelegate.MODE_NIGHT_NO
                Theme.DARK -> AppCompatDelegate.MODE_NIGHT_YES
            }
        )
    }

    override fun showCurrentTheme(theme: Theme) {
        binding.settingsThemeSwitch.isChecked = when (theme) {
            Theme.LIGHT -> false
            Theme.DARK -> true
        }
    }
}