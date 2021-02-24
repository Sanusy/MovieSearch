package com.gmail.ivan.morozyk.moviesearch.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import androidx.core.view.forEach
import com.gmail.ivan.morozyk.moviesearch.R
import com.gmail.ivan.morozyk.moviesearch.data.Title
import com.gmail.ivan.morozyk.moviesearch.databinding.FragmentTitleListBinding
import com.gmail.ivan.morozyk.moviesearch.extentions.makeGone
import com.gmail.ivan.morozyk.moviesearch.extentions.makeInvisible
import com.gmail.ivan.morozyk.moviesearch.extentions.makeVisible
import com.gmail.ivan.morozyk.moviesearch.mvvm.Resource
import com.gmail.ivan.morozyk.moviesearch.mvvm.viewmodel.QueryType
import com.gmail.ivan.morozyk.moviesearch.mvvm.viewmodel.TitleListViewModel
import com.gmail.ivan.morozyk.moviesearch.ui.activity.ItemSelected
import com.gmail.ivan.morozyk.moviesearch.ui.adapter.TitleAdapter
import com.google.android.material.chip.Chip
import org.koin.android.viewmodel.ext.android.viewModel

class TitleListFragment : BaseFragment<FragmentTitleListBinding>() {

    private lateinit var adapter: TitleAdapter

    private lateinit var searchView: SearchView

    private val viewModel: TitleListViewModel by viewModel()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentTitleListBinding.inflate(inflater, container, attachToRoot)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            viewModel.titledData.observe(viewLifecycleOwner) {
                when (it) {
                    is Resource.Loading -> {
                        showProgress()
                    }
                    is Resource.Success -> {
                        if (it.data.isNullOrEmpty()) {
                            showEmpty()
                            return@observe
                        }
                        showTitleList(it.data)
                    }
                    is Resource.HttpError.InternalServerError -> showError(R.string.title_list_internal_server_error)
                    is Resource.HttpError.NoInternetError -> showError(R.string.title_list_no_internet_string)
                    is Resource.HttpError.NotAuthorizedError -> showError(R.string.title_list_not_authorized_error)
                    is Resource.HttpError.NotFoundError -> showError(R.string.title_list_not_found_error)
                    is Resource.HttpError.UnknownError -> showError(R.string.title_list_unknown_error_string)
                }
            }

            adapter = TitleAdapter{ viewModel.onTitleClicked(it) }

            titleRecycler.adapter = adapter
            titleListPullToRefresh.setOnRefreshListener { viewModel.refresh() }

            titleListChipGroup.setOnCheckedChangeListener { group, _ ->
                group.forEach {
                    val chip = (it as Chip)
                    chip.isEnabled = !chip.isChecked
                }
            }

            topMoviesChip.isChecked = true

            topMoviesChip.setOnClickListener { viewModel.getTitleList(QueryType.TOP_250_MOVIES) }
            topSeriesChip.setOnClickListener { viewModel.getTitleList(QueryType.TOP_250_SERIES) }
            mostPopularMoviesChip.setOnClickListener { viewModel.getTitleList(QueryType.MOST_POPULAR_MOVIES) }
            mostPopularSeriesChip.setOnClickListener { viewModel.getTitleList(QueryType.MOST_POPULAR_SERIES) }
            inTheatresChip.setOnClickListener { viewModel.getTitleList(QueryType.IN_THEATRES) }
            comingSoonChip.setOnClickListener { viewModel.getTitleList(QueryType.COMING_SOON) }

            searchView = titleListToolbar.menu.findItem(R.id.search_title).actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    binding.titleListChipGroup.forEach { (it as Chip).isChecked = false }
                    viewModel.searchTitleByWord(query!!)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    //none
                    return true
                }
            })

            val closeButton: View? =
                searchView.findViewById(androidx.appcompat.R.id.search_close_btn)
            closeButton?.setOnClickListener {
                clearSearch()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        requireMainActivity().setSelectedItem(ItemSelected.TITLES)
    }

    private fun showError(@StringRes errorStringId: Int) = with(binding) {
        hideProgress()
        noContentText.makeVisible()
        noContentText.text = getString(errorStringId)
    }

    private fun showEmpty() {
        hideProgress()
        with(binding) {
            noContentText.makeVisible()
            noContentText.text = getString(R.string.title_list_no_content_string)
        }
    }

    private fun showTitleList(titleList: List<Title>) {
        hideProgress()
        adapter.showTitles(titleList)
        binding.titleRecycler.makeVisible()
    }

    private fun clearSearch() {
        searchView.setQuery("", false)
        with(binding) {
            noContentText.makeVisible()
            titleRecycler.makeInvisible()
            noContentText.text = getString(R.string.title_list_clear_search)
            titleListPullToRefresh.isEnabled = false
        }
    }

    private fun showProgress() {
        with(binding) {
            titleListPullToRefresh.isRefreshing = false
            titleRecycler.makeInvisible()
            titleLoadingProgress.show()
            noContentText.makeGone()
        }
    }

    private fun hideProgress() = with(binding) {
        titleListPullToRefresh.isEnabled = true
        titleLoadingProgress.hide()
    }
}