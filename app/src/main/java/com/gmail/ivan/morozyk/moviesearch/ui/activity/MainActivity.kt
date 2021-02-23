package com.gmail.ivan.morozyk.moviesearch.ui.activity

import android.os.Bundle
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.gmail.ivan.morozyk.moviesearch.R
import com.gmail.ivan.morozyk.moviesearch.databinding.ActivityMainBinding
import moxy.MvpAppCompatActivity

class MainActivity : MvpAppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigationView.setupWithNavController(navController)
    }

    fun setSelectedItem(item: ItemSelected) {
        binding.bottomNavigationView.menu.findItem(item.itemId).isChecked = true
    }
}

enum class ItemSelected(val itemId: Int) {
    TITLES(R.id.titleListFragment),
    PERSONS(R.id.personListFragment),
    SETTINGS(R.id.settingsFragment)
}