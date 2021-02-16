package com.gmail.ivan.morozyk.moviesearch.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class TitleDto(
    val id: String,
    val title: String,
    var type: String?,
    val year: String?,
    val image: String?,
    val runtimeStr: String?,
    val plot: String?,
    val releaseDate: String?,
    val actorList: List<PersonDto>?,
    val genres: String?,
    val companies: String?,
    val imDbRating: String?,
    val description: String?
)

@JsonClass(generateAdapter = true)
data class SearchTitleRootDto(val results: List<TitleDto>)

@JsonClass(generateAdapter = true)
data class GetTitlesRootDto(val items: List<TitleDto>)
