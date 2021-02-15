package com.gmail.ivan.morozyk.moviesearch.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PersonDto(val id: String, val name: String?, val image: String?, val asCharacter: String?, val title: String?)

@JsonClass(generateAdapter = true)
data class SearchPersonRootDto(val results: List<PersonDto>)
