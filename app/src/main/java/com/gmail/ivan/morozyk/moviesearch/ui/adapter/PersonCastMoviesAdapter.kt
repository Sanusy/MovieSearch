package com.gmail.ivan.morozyk.moviesearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gmail.ivan.morozyk.moviesearch.data.Title
import com.gmail.ivan.morozyk.moviesearch.databinding.ItemCastTitleBinding
import com.gmail.ivan.morozyk.moviesearch.extentions.makeGone
import com.gmail.ivan.morozyk.moviesearch.ui.fragment.PersonDetailsFragmentDirections

class PersonCastMoviesAdapter :
    ListAdapter<Title, PersonCastMoviesAdapter.TitleViewHolder>(
        titleDiffUtil
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = TitleViewHolder(
        ItemCastTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: TitleViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class TitleViewHolder(private val binding: ItemCastTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(title: Title) = with(binding) {
            root.setOnClickListener {
                val action =
                    PersonDetailsFragmentDirections.actionPersonDetailsFragmentToTitleDetailsFragment(
                        title.id
                    )
                root.findNavController().navigate(action)
            }
            itemCastMovieTitle.text = title.name

            if (title.year.isNullOrEmpty()) {
                itemCastMovieYear.makeGone()
                return
            }
            itemCastMovieYear.text = title.year
        }
    }
}

private val titleDiffUtil = object : DiffUtil.ItemCallback<Title>() {
    override fun areItemsTheSame(oldItem: Title, newItem: Title) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Title, newItem: Title) = oldItem == newItem
}