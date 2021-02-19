package com.gmail.ivan.morozyk.moviesearch.ui.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import com.gmail.ivan.morozyk.moviesearch.R
import com.gmail.ivan.morozyk.moviesearch.databinding.ActivityMainBinding
import com.gmail.ivan.morozyk.moviesearch.ui.fragment.PersonListFragment
import com.gmail.ivan.morozyk.moviesearch.ui.fragment.SettingsFragment
import com.gmail.ivan.morozyk.moviesearch.ui.fragment.TitleListFragment
import moxy.MvpAppCompatActivity

class MainActivity : MvpAppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.bottomNavigationView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.titles_menu_item -> {
                    navigate(TitleListFragment.newInstance())
                    true
                }
                R.id.persons_menu_item -> {
                    navigate(PersonListFragment.newInstance())
                    true
                }
                R.id.settings_menu_item -> {
                    navigate(SettingsFragment.newInstance())
                    true
                }
                else -> false
            }
        }

        if (savedInstanceState == null) {
            binding.bottomNavigationView.selectedItemId = R.id.titles_menu_item
        }
    }

    fun setSelectedItem(item: ItemSelected) {
        binding.bottomNavigationView.menu.findItem(item.itemId).isChecked = true
    }

    private fun navigate(navigateTo: Fragment) {
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            replace(R.id.fragment_container, navigateTo)
        }
    }
}

enum class ItemSelected(val itemId: Int) {
    TITLES(R.id.titles_menu_item),
    PERSONS(R.id.persons_menu_item),
    SETTINGS(R.id.settings_menu_item)
}