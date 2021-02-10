package com.gmail.ivan.morozyk.moviesearch.data

import androidx.recyclerview.widget.DiffUtil

data class Person(val id: String, val name: String, val image: String?, val asCharacter: String?) {

    object PersonDifUtil : DiffUtil.ItemCallback<Person>() {
        override fun areItemsTheSame(oldItem: Person, newItem: Person) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Person, newItem: Person) = oldItem.id == newItem.id
    }
}
