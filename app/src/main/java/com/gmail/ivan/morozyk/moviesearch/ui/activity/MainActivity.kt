package com.gmail.ivan.morozyk.moviesearch.ui.activity

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.gmail.ivan.morozyk.moviesearch.R
import com.gmail.ivan.morozyk.moviesearch.databinding.ActivityMainBinding
import com.gmail.ivan.morozyk.moviesearch.navigation.Action
import com.gmail.ivan.morozyk.moviesearch.navigation.Router
import com.gmail.ivan.morozyk.moviesearch.navigation.RoutingProvider
import com.gmail.ivan.morozyk.moviesearch.ui.fragment.PersonDetailsFragmentDirections
import com.gmail.ivan.morozyk.moviesearch.ui.fragment.PersonListFragmentDirections
import com.gmail.ivan.morozyk.moviesearch.ui.fragment.TitleDetailsFragmentDirections
import com.gmail.ivan.morozyk.moviesearch.ui.fragment.TitleListFragmentDirections
import moxy.MvpAppCompatActivity
import org.koin.android.ext.android.inject

class MainActivity : MvpAppCompatActivity(), RoutingProvider {

    private val router: Router by inject()

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        router.attachRoutingProvider(this)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    override fun onDestroy() {
        super.onDestroy()

        router.detachRoutingProvider()
    }

    fun setSelectedItem(item: ItemSelected) {
        binding.bottomNavigationView.menu.findItem(item.itemId).isChecked = true
    }

    override fun navigate(navigationAction: Action) {
        navController.navigate(
            when (navigationAction) {
                is Action.FromPersonDetailsToTitleDetails -> {
                    PersonDetailsFragmentDirections.fromPersonDetailsToTitleDetails(
                        navigationAction.titleId
                    )
                }
                is Action.FromPersonListToPersonDetails -> {
                    PersonListFragmentDirections.fromPersonListToPersonDetails(
                        navigationAction.personId
                    )
                }
                is Action.FromTitleDetailsToPersonDetails -> {
                    TitleDetailsFragmentDirections.fromTitleDetailsToPersonDetails(
                        navigationAction.personId
                    )
                }
                is Action.FromTitleListToTitleDetails -> {
                    TitleListFragmentDirections.fromTitleListToTitleDetails(
                        navigationAction.titleId
                    )
                }
            }
        )
    }
}

enum class ItemSelected(val itemId: Int) {
    TITLES(R.id.titleListFragment),
    PERSONS(R.id.personListFragment),
    SETTINGS(R.id.settingsFragment)
}