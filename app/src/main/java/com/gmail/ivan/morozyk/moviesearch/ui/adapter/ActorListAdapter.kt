package com.gmail.ivan.morozyk.moviesearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmail.ivan.morozyk.moviesearch.R
import com.gmail.ivan.morozyk.moviesearch.data.Person
import com.gmail.ivan.morozyk.moviesearch.databinding.ItemActorBinding
import com.gmail.ivan.morozyk.moviesearch.ui.fragment.TitleDetailsFragmentDirections


class ActorListAdapter :
    ListAdapter<Person, ActorListAdapter.PersonHolder>(personDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PersonHolder(
        ItemActorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: PersonHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PersonHolder(private val binding: ItemActorBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(person: Person) {
            with(binding) {
                root.setOnClickListener {
                    val action =
                        TitleDetailsFragmentDirections.actionTitleDetailsFragmentToPersonDetailsFragment(
                            person.id
                        )
                    root.findNavController().navigate(action)
                }
                actorName.text = person.name
                actorAsCharacter.text =
                    root.context.getString(R.string.title_details_as_character, person.asCharacter)

                Glide.with(root.context).load(person.image).circleCrop()
                    .placeholder(R.drawable.icon_title_placeholder).into(actorImage)
            }
        }
    }
}

private val personDiffUtil = object : DiffUtil.ItemCallback<Person>() {
    override fun areItemsTheSame(oldItem: Person, newItem: Person) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Person, newItem: Person) = oldItem == newItem
}