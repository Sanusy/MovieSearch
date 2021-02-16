package com.gmail.ivan.morozyk.moviesearch.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.forEach
import androidx.fragment.app.commit
import com.gmail.ivan.morozyk.moviesearch.R
import com.gmail.ivan.morozyk.moviesearch.data.Title
import com.gmail.ivan.morozyk.moviesearch.data.service.HttpError
import com.gmail.ivan.morozyk.moviesearch.databinding.FragmentTitleListBinding
import com.gmail.ivan.morozyk.moviesearch.extentions.makeGone
import com.gmail.ivan.morozyk.moviesearch.extentions.makeInvisible
import com.gmail.ivan.morozyk.moviesearch.extentions.makeVisible
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.QueryType
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.TitleListContract
import com.gmail.ivan.morozyk.moviesearch.mvp.presenter.TitleListPresenter
import com.gmail.ivan.morozyk.moviesearch.ui.adapter.TitleAdapter
import com.google.android.material.chip.Chip
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.get

class TitleListFragment : BaseFragment<FragmentTitleListBinding>(), TitleListContract.View {

    @InjectPresenter
    lateinit var presenter: TitleListPresenter

    @ProvidePresenter
    fun providePresenter(): TitleListPresenter = get()

    private lateinit var adapter: TitleAdapter

    private lateinit var searchView: SearchView

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentTitleListBinding.inflate(inflater, container, attachToRoot)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {

            adapter = TitleAdapter { titleId ->
                parentFragmentManager.commit {
                    setReorderingAllowed(true)
                    addToBackStack(null)
                    replace(R.id.fragment_container, TitleDetailsFragment.newInstance(titleId))
                }
            }

            titleRecycler.adapter = adapter
            titleListPullToRefresh.setOnRefreshListener { presenter.refresh() }

            titleListChipGroup.setOnCheckedChangeListener { group, _ ->
                group.forEach {
                    val chip = (it as Chip)
                    chip.isEnabled = !chip.isChecked
                }
            }

            topMoviesChip.isChecked = true

            topMoviesChip.setOnClickListener { presenter.getTitleList(QueryType.TOP_250_MOVIES) }
            topSeriesChip.setOnClickListener { presenter.getTitleList(QueryType.TOP_250_SERIES) }
            mostPopularMoviesChip.setOnClickListener { presenter.getTitleList(QueryType.MOST_POPULAR_MOVIES) }
            mostPopularSeriesChip.setOnClickListener { presenter.getTitleList(QueryType.MOST_POPULAR_SERIES) }
            inTheatresChip.setOnClickListener { presenter.getTitleList(QueryType.IN_THEATRES) }
            comingSoonChip.setOnClickListener { presenter.getTitleList(QueryType.COMING_SOON) }

            searchView = titleListToolbar.menu.findItem(R.id.search_title).actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    binding.titleListChipGroup.forEach { (it as Chip).isChecked = false }
                    presenter.searchTitleByWord(query!!)
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
                presenter.clearSearchButtonClicked()
            }
        }
    }

    override fun showError(error: HttpError) = with(binding) {
        noContentText.makeVisible()
        noContentText.text = getString(
            when (error) {
                HttpError.InternalServerError -> R.string.title_list_internal_server_error
                HttpError.NoInternetError -> R.string.title_list_no_internet_string
                HttpError.NotAuthorizedError -> R.string.title_list_not_authorized_error
                HttpError.NotFoundError -> R.string.title_list_not_found_error
                HttpError.UnknownError -> R.string.title_list_unknown_error_string
            }
        )
    }

    override fun showEmpty() {
        with(binding) {
            noContentText.makeVisible()
            noContentText.text = getString(R.string.title_list_no_content_string)
        }
    }

    override fun showTitleList(titleList: List<Title>) {
        adapter.showTitles(titleList)
        binding.titleRecycler.makeVisible()
    }

    override fun clearSearch() {
        searchView.setQuery("", false)
        with(binding) {
            noContentText.makeVisible()
            titleRecycler.makeInvisible()
            noContentText.text = getString(R.string.title_list_clear_search)
        }
    }

    override fun showProgress() {
        with(binding) {
            titleListPullToRefresh.isRefreshing = false
            titleRecycler.makeInvisible()
            titleLoadingProgress.show()
            noContentText.makeGone()
        }
    }

    override fun hideProgress() {
        binding.titleLoadingProgress.hide()
    }

    companion object {
        fun newInstance() = TitleListFragment()
    }
}