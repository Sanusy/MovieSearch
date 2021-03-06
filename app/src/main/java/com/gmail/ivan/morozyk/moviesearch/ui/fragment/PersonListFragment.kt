package com.gmail.ivan.morozyk.moviesearch.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import com.gmail.ivan.morozyk.moviesearch.R
import com.gmail.ivan.morozyk.moviesearch.data.Person
import com.gmail.ivan.morozyk.moviesearch.data.mapper.HttpError
import com.gmail.ivan.morozyk.moviesearch.databinding.FragmentPersonListBinding
import com.gmail.ivan.morozyk.moviesearch.extentions.makeInvisible
import com.gmail.ivan.morozyk.moviesearch.extentions.makeVisible
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.PersonListContract
import com.gmail.ivan.morozyk.moviesearch.mvp.presenter.PersonListPresenter
import com.gmail.ivan.morozyk.moviesearch.ui.activity.ItemSelected
import com.gmail.ivan.morozyk.moviesearch.ui.adapter.PersonListAdapter
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.get

class PersonListFragment : BaseFragment<FragmentPersonListBinding>(), PersonListContract.View {

    @InjectPresenter
    lateinit var presenter: PersonListPresenter

    @ProvidePresenter
    fun providePresenter(): PersonListPresenter = get()

    private lateinit var adapter: PersonListAdapter

    private lateinit var searchView: SearchView

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentPersonListBinding.inflate(inflater, container, attachToRoot)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PersonListAdapter { presenter.onPersonClicked(it) }

        with(binding) {
            personRecycler.adapter = adapter

            personListPullToRefresh.isEnabled = false

            searchView =
                personListToolbar.menu.findItem(R.id.search_person).actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

                override fun onQueryTextSubmit(query: String?): Boolean {
                    presenter.searchPerson(query!!)
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

            personListPullToRefresh.setOnRefreshListener { presenter.refresh() }
        }
    }

    override fun onStart() {
        super.onStart()

        requireMainActivity().setSelectedItem(ItemSelected.PERSONS)
    }

    override fun showPersons(personList: List<Person>) {
        binding.personRecycler.makeVisible()
        adapter.submitList(personList)
    }

    override fun showError(error: HttpError) = with(binding) {
        personListDialogText.makeVisible()
        personListDialogText.text = getString(
            when (error) {
                HttpError.InternalServerError -> R.string.person_list_internal_server_error
                HttpError.NoInternetError -> R.string.person_list_no_internet_string
                HttpError.NotAuthorizedError -> R.string.person_list_not_authorized_error
                HttpError.NotFoundError -> R.string.person_list_not_found_error
                HttpError.UnknownError -> R.string.person_list_unknown_error_string
            }
        )
    }

    override fun showEmpty() = with(binding) {
        personListDialogText.makeVisible()
        personListDialogText.text = getString(R.string.person_list_no_content_string)
    }

    override fun showProgress() = with(binding) {
        personListPullToRefresh.isRefreshing = false
        personListPullToRefresh.isEnabled = true
        personListProgress.show()
        personRecycler.makeInvisible()
        personListDialogText.makeInvisible()
    }

    override fun hideProgress() {
        binding.personListProgress.hide()
    }

    override fun clearSearch() {
        searchView.setQuery("", false)
        with(binding) {
            personListPullToRefresh.isRefreshing = false
            personListPullToRefresh.isEnabled = false
            personListDialogText.makeVisible()
            personRecycler.makeInvisible()
            personListDialogText.text = getString(R.string.person_list_clear_search)
        }
    }
}