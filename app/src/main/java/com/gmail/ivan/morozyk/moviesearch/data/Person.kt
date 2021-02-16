package com.gmail.ivan.morozyk.moviesearch.data

data class Person(
    val id: String,
    val name: String,
    val image: String?,
    val asCharacter: String?,
    val role: String?,
    val age: Int?,
    val summary: String?,
    val birthDate: String?,
    val deathDate: String?,
    val awards: String?,
    val castMovies: List<Title>?
)