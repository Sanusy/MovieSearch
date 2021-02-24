package com.gmail.ivan.morozyk.moviesearch.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.bumptech.glide.Glide
import com.gmail.ivan.morozyk.moviesearch.R
import com.gmail.ivan.morozyk.moviesearch.data.Person
import com.gmail.ivan.morozyk.moviesearch.data.mapper.HttpError
import com.gmail.ivan.morozyk.moviesearch.databinding.FragmentPersonDetailsBinding
import com.gmail.ivan.morozyk.moviesearch.extentions.makeGone
import com.gmail.ivan.morozyk.moviesearch.extentions.makeVisible
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.PersonDetailsContract
import com.gmail.ivan.morozyk.moviesearch.mvp.presenter.PersonDetailsPresenter
import com.gmail.ivan.morozyk.moviesearch.ui.activity.ItemSelected
import com.gmail.ivan.morozyk.moviesearch.ui.adapter.PersonCastMoviesAdapter
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.get

class PersonDetailsFragment : BaseFragment<FragmentPersonDetailsBinding>(),
    PersonDetailsContract.View {

    @InjectPresenter
    lateinit var presenter: PersonDetailsPresenter

    @ProvidePresenter
    fun providePresenter(): PersonDetailsPresenter = get()

    private val args: PersonDetailsFragmentArgs by navArgs()

    private lateinit var adapter: PersonCastMoviesAdapter

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentPersonDetailsBinding.inflate(inflater, container, attachToRoot)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = PersonCastMoviesAdapter { presenter.onTitleClicked(it) }

        with(binding) {
            (requireActivity() as AppCompatActivity).setSupportActionBar(personDetailsToolbar)
            val navController = findNavController()
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            personDetailsToolbar.setupWithNavController(navController, appBarConfiguration)

            personDetailsCastMoviesRecycler.adapter = adapter
            personDetailsCastMoviesRecycler.addItemDecoration(
                DividerItemDecoration(
                    personDetailsCastMoviesRecycler.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()

        requireMainActivity().setSelectedItem(ItemSelected.PERSONS)
        presenter.loadPerson(args.personId)
    }

    override fun showPerson(person: Person) = with(binding) {
        Glide.with(this@PersonDetailsFragment).load(person.image).centerCrop()
            .placeholder(R.drawable.icon_title_placeholder)
            .into(personDetailsPoster)

        personDetailsAppbarLayout.setExpanded(true, true)

        binding.personDetailsToolbarLayout.title = person.name

        if (person.birthDate.isNullOrEmpty()) {
            birthDateGroup.makeGone()
        } else {
            birthDateGroup.makeVisible()
            if (person.deathDate == null) {
                personDetailsBirthDateText.text =
                    getString(R.string.person_details_alive_string, person.birthDate, person.age)
                deathDateGroup.makeGone()
            } else {
                deathDateGroup.makeVisible()
                personDetailsDeathDateText.text =
                    getString(R.string.person_details_died_string, person.deathDate, person.age)
                personDetailsBirthDateText.text = person.birthDate
            }
        }

        if (person.role.isNullOrEmpty()) {
            roleGroup.makeGone()
        } else {
            roleGroup.makeVisible()
            personDetailsRoleText.text = person.role
        }

        if (person.awards.isNullOrEmpty()) {
            awardsGroup.makeGone()
        } else {
            awardsGroup.makeVisible()
            personDetailsAwardsText.text = person.awards
        }

        if (person.summary.isNullOrEmpty()) {
            summaryGroup.makeGone()
        } else {
            summaryGroup.makeVisible()
            personDetailsSummaryText.text = person.summary
        }

        if (person.castMovies.isNullOrEmpty()) {
            personDetailsCastMoviesRecycler.makeGone()
        } else {
            personDetailsCastMoviesRecycler.makeVisible()
            adapter.submitList(person.castMovies)
        }
    }

    override fun showError(error: HttpError) = with(binding) {
        personDetailsDialogText.makeVisible()
        personDetailsDialogText.text = getString(
            when (error) {
                HttpError.InternalServerError -> R.string.person_details_internal_server_error
                HttpError.NoInternetError -> R.string.person_details_no_internet_string
                HttpError.NotAuthorizedError -> R.string.person_details_not_authorized_error
                HttpError.NotFoundError -> R.string.person_details_not_found_error
                HttpError.UnknownError -> R.string.person_details_unknown_error_string
            }
        )
    }

    override fun showProgress() = with(binding) {
        personDetailsDialogText.makeGone()
        personDetailsProgress.show()
    }

    override fun hideProgress() {
        binding.personDetailsProgress.hide()
    }
}