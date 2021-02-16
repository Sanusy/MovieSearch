package com.gmail.ivan.morozyk.moviesearch.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.gmail.ivan.morozyk.moviesearch.R
import com.gmail.ivan.morozyk.moviesearch.data.Person
import com.gmail.ivan.morozyk.moviesearch.databinding.ItemPersonBinding

class PersonListAdapter(private val onPersonClicked: (String) -> Unit) :
    ListAdapter<Person, PersonListAdapter.PersonHolder>(personDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PersonHolder(
        ItemPersonBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: PersonHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class PersonHolder(private val binding: ItemPersonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(person: Person) = with(binding) {

            root.setOnClickListener { onPersonClicked(person.id) }

            Glide.with(root).load(person.image).circleCrop()
                .placeholder(R.drawable.icon_title_placeholder).into(personItemImage)

            personItemName.text = person.name
        }
    }
}

private val personDiffUtil = object : DiffUtil.ItemCallback<Person>() {
    override fun areItemsTheSame(oldItem: Person, newItem: Person) = oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Person, newItem: Person) = oldItem == newItem
}