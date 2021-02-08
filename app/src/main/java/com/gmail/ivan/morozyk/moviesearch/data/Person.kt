package com.gmail.ivan.morozyk.moviesearch.data

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Person(val id: String)
