package com.gmail.ivan.morozyk.moviesearch.ui.fragment

import android.os.Bundle
import android.view.*
import androidx.annotation.StringRes
import androidx.appcompat.widget.SearchView
import com.gmail.ivan.morozyk.moviesearch.R
import com.gmail.ivan.morozyk.moviesearch.data.Title
import com.gmail.ivan.morozyk.moviesearch.data.service.TitleServiceImpl
import com.gmail.ivan.morozyk.moviesearch.databinding.FragmentTitleListBinding
import com.gmail.ivan.morozyk.moviesearch.extentions.makeGone
import com.gmail.ivan.morozyk.moviesearch.extentions.makeInvisible
import com.gmail.ivan.morozyk.moviesearch.extentions.makeVisible
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.QueryType
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.TitleListContract
import com.gmail.ivan.morozyk.moviesearch.mvp.presenter.TitleListPresenter
import com.gmail.ivan.morozyk.moviesearch.ui.adapter.TitleAdapter
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter

class TitleListFragment : BaseFragment<FragmentTitleListBinding>(), TitleListContract.View {

    @InjectPresenter
    lateinit var presenter: TitleListPresenter

    private lateinit var adapter: TitleAdapter

    @ProvidePresenter
    fun providePresenter(): TitleListPresenter {
        val service = TitleServiceImpl()
        return TitleListPresenter(service)
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentTitleListBinding.inflate(inflater, container, attachToRoot)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

        adapter = TitleAdapter { titleId -> }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.titleRecycler.adapter = adapter
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_title_list, menu)

        val searchView = menu.findItem(R.id.search_title).actionView as SearchView
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                presenter.searchTitleByWord(query!!)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                //none
                return true
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.top_250_movies -> {
            presenter.getTitleList(QueryType.TOP_250_MOVIES)
            true
        }
        R.id.top_250_series -> {
            presenter.getTitleList(QueryType.TOP_250_SERIES)
            true
        }
        R.id.most_popular_movies -> {
            presenter.getTitleList(QueryType.MOST_POPULAR_MOVIES)
            true
        }
        R.id.most_popular_series -> {
            presenter.getTitleList(QueryType.MOST_POPULAR_SERIES)
            true
        }
        R.id.in_theatres -> {
            presenter.getTitleList(QueryType.IN_THEATRES)
            true
        }
        R.id.coming_soon -> {
            presenter.getTitleList(QueryType.COMING_SOON)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    override fun showInternetConnectionError() {
        with(binding) {
            noContentText.makeVisible()
            noContentText.text = getString(R.string.title_list_no_internet_string)
        }
    }

    override fun showEmptyContentError() {
        with(binding) {
            noContentText.makeVisible()
            noContentText.text = getString(R.string.title_list_no_content_string)
        }
    }

    override fun showUnknownError() {
        with(binding) {
            noContentText.makeVisible()
            noContentText.text = getString(R.string.title_list_unknown_error_string)
        }
    }

    override fun showTitleList(titleList: List<Title>) {
        adapter.showTitles(titleList)
        binding.titleRecycler.makeVisible()
    }

    override fun showProgress() {
        with(binding) {
            titleRecycler.makeInvisible()
            titleLoadingProgress.show()
            noContentText.makeGone()
        }
    }

    override fun hideProgress() {
        binding.titleLoadingProgress.hide()
    }

    override fun setAppTitle(queryType: QueryType?) {
        requireActivity().title =
            getString(if (queryType == null) R.string.app_name else getTitleString(queryType))
    }

    @StringRes
    private fun getTitleString(queryType: QueryType) = when (queryType) {
        QueryType.TOP_250_MOVIES -> R.string.title_list_top_250_movies_string
        QueryType.TOP_250_SERIES -> R.string.title_list_top_250_series_string
        QueryType.MOST_POPULAR_MOVIES -> R.string.title_list_most_popular_movies
        QueryType.MOST_POPULAR_SERIES -> R.string.title_list_most_popular_series
        QueryType.IN_THEATRES -> R.string.title_list_in_theatres
        QueryType.COMING_SOON -> R.string.title_list_coming_soon
    }

    companion object {
        fun newInstance() = TitleListFragment()
    }
}