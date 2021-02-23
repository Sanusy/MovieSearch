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
import com.gmail.ivan.morozyk.moviesearch.data.Title
import com.gmail.ivan.morozyk.moviesearch.data.mapper.HttpError
import com.gmail.ivan.morozyk.moviesearch.databinding.FragmentTitleDetailsBinding
import com.gmail.ivan.morozyk.moviesearch.extentions.makeGone
import com.gmail.ivan.morozyk.moviesearch.extentions.makeVisible
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.TitleDetailsContract
import com.gmail.ivan.morozyk.moviesearch.mvp.presenter.TitleDetailsPresenter
import com.gmail.ivan.morozyk.moviesearch.ui.activity.ItemSelected
import com.gmail.ivan.morozyk.moviesearch.ui.adapter.ActorListAdapter
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import org.koin.android.ext.android.get

class TitleDetailsFragment : BaseFragment<FragmentTitleDetailsBinding>(),
    TitleDetailsContract.View {

    @InjectPresenter
    lateinit var presenter: TitleDetailsPresenter

    private lateinit var adapter: ActorListAdapter

    @ProvidePresenter
    fun providePresenter(): TitleDetailsPresenter = get()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ) = FragmentTitleDetailsBinding.inflate(inflater, container, attachToRoot)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
            val navController = findNavController()
            val appBarConfiguration = AppBarConfiguration(navController.graph)
            toolbar.setupWithNavController(navController, appBarConfiguration)

            adapter = ActorListAdapter()
            starListRecycler.adapter = adapter

            starListRecycler.addItemDecoration(
                DividerItemDecoration(
                    starListRecycler.context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    override fun onStart() {
        super.onStart()

        requireMainActivity().setSelectedItem(ItemSelected.TITLES)
        presenter.loadTitle(navArgs<TitleDetailsFragmentArgs>().value.titleId)
    }

    override fun showTitle(title: Title) = with(binding) {
        Glide.with(this@TitleDetailsFragment).load(title.image).centerCrop()
            .placeholder(R.drawable.icon_title_placeholder)
            .into(titlePoster)

        titleDetailsAppbarLayout.setExpanded(true, true)

        binding.toolbarTitleLayout.title = title.name

        if (!title.runTime.isNullOrEmpty()) {
            titleRuntimeText.text =
                getString(R.string.title_details_runtime_string, title.runTime)
        } else {
            titleRuntimeText.makeGone()
        }

        if (!title.releaseDate.isNullOrEmpty()) {
            titleReleaseDate.text =
                getString(R.string.title_details_release_date, title.releaseDate)
        } else {
            titleReleaseDate.makeGone()
        }

        if (!title.rating.isNullOrEmpty()) {
            titleRatingIcon.makeVisible()
            titleRatingText.text = title.rating
        } else {
            titleRatingText.makeGone()
            titleRatingIcon.makeGone()
        }

        if (!title.genres.isNullOrEmpty()) {
            titleGenresText.text = getString(R.string.title_details_genres, title.genres)
        } else {
            titleGenresText.makeGone()
        }

        if (!title.companies.isNullOrEmpty()) {
            titleCompaniesText.text =
                getString(R.string.title_details_companies, title.companies)
        } else {
            titleCompaniesText.makeGone()
        }

        if (!title.plot.isNullOrEmpty()) {
            titleDetailsPlotLabel.makeVisible()
            titlePlotText.text = title.plot
        } else {
            titlePlotText.makeGone()
        }

        if (!title.actorList.isNullOrEmpty()) {
            titleDetailsStarsLabel.makeVisible()
            adapter.submitList(title.actorList)
        }
    }

    override fun showError(error: HttpError) = with(binding) {
        titleDetailsDialogText.makeVisible()
        titleDetailsDialogText.text = getString(
            when (error) {
                HttpError.InternalServerError -> R.string.title_details_internal_server_error
                HttpError.NoInternetError -> R.string.title_details_no_internet_string
                HttpError.NotAuthorizedError -> R.string.title_details_not_authorized_error
                HttpError.NotFoundError -> R.string.title_details_not_found_error
                HttpError.UnknownError -> R.string.title_details_unknown_error_string
            }
        )
    }

    override fun showProgress() = with(binding) {
        titleDetailsDialogText.makeGone()
        titleDetailsProgress.show()
    }

    override fun hideProgress() {
        binding.titleDetailsProgress.hide()
    }
}