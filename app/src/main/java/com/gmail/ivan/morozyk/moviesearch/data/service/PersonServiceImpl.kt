package com.gmail.ivan.morozyk.moviesearch.data.service

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.awaitResult
import com.github.kittinunf.fuel.moshi.moshiDeserializerOf
import com.github.kittinunf.result.Result
import com.gmail.ivan.morozyk.moviesearch.BuildConfig
import com.gmail.ivan.morozyk.moviesearch.data.PersonDto
import com.gmail.ivan.morozyk.moviesearch.data.SearchPersonRootDto

class PersonServiceImpl : PersonService {

    override suspend fun getPersonList(query: String): Result<SearchPersonRootDto, FuelError> =
        Fuel.get("/SearchName/$API_KEY/$query").awaitResult(
            moshiDeserializerOf(SearchPersonRootDto::class.java)
        )

    override suspend fun getPersonById(personId: String): Result<PersonDto, FuelError> =
        Fuel.get("/Name/$API_KEY/$personId").awaitResult(
            moshiDeserializerOf(PersonDto::class.java)
        )


    companion object {
        private const val API_KEY = BuildConfig.IMDB_API_KEY
    }
}