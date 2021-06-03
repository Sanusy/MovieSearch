package com.gmail.ivan.morozyk.moviesearch.data

data class Title(
    val id: String,
    val name: String,
    val type: String?,
    val year: String?,
    val image: String? = null,
    val runTime: String? = null,
    val plot: String? = null,
    val releaseDate: String? = null,
    val actorList: List<Person>? = null,
    val genres: String? = null,
    val companies: String? = null,
    val rating: String?
)
