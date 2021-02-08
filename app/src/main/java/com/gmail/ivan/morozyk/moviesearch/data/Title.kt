package com.gmail.ivan.morozyk.moviesearch.data

data class Title(
    val id: String,
    val name: String,
    val type: String?,
    val year: String?,
    val image: String,
    val runTime: String?,
    val plot: String?,
    val starList: List<Person>?,
    val genres: String?,
    val companies: String?,
    val rating: String?
)
