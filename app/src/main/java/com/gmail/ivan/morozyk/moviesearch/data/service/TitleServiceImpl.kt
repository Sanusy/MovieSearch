package com.gmail.ivan.morozyk.moviesearch.data.service

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.awaitResult
import com.github.kittinunf.fuel.moshi.moshiDeserializerOf
import com.github.kittinunf.result.Result
import com.gmail.ivan.morozyk.moviesearch.BuildConfig
import com.gmail.ivan.morozyk.moviesearch.data.GetTitlesRootDto
import com.gmail.ivan.morozyk.moviesearch.data.SearchTitleRootDto
import com.gmail.ivan.morozyk.moviesearch.data.TitleDto

class TitleServiceImpl : TitleService {

    override suspend fun getTitlesByQuery(query: String): Result<GetTitlesRootDto, FuelError> =
        Fuel.get("/$query/$API_KEY")
            .awaitResult(moshiDeserializerOf(GetTitlesRootDto::class.java))

    override suspend fun searchTitlesByWord(query: String): Result<SearchTitleRootDto, FuelError> =
        Fuel.get("/SearchTitle/$API_KEY/$query")
            .awaitResult(moshiDeserializerOf(SearchTitleRootDto::class.java))

    override suspend fun getTitleById(titleId: String): Result<TitleDto, FuelError> =
        Fuel.get("/Title/$API_KEY/$titleId").awaitResult(
            moshiDeserializerOf(TitleDto::class.java)
        )

    companion object {
        private const val API_KEY = BuildConfig.IMDB_API_KEY
    }
}