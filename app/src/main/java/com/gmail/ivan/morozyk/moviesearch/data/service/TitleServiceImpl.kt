package com.gmail.ivan.morozyk.moviesearch.data.service

import com.github.kittinunf.fuel.Fuel
import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.fuel.core.awaitResult
import com.github.kittinunf.fuel.moshi.moshiDeserializerOf
import com.github.kittinunf.result.Result
import com.gmail.ivan.morozyk.moviesearch.data.GetTitlesRootDto
import com.gmail.ivan.morozyk.moviesearch.data.SearchTitleRootDto

class TitleServiceImpl : TitleService {

    override suspend fun getTitlesByQuery(query: String): Result<GetTitlesRootDto, FuelError> {
        return Fuel.get("/$query/k_956nmhwo")
            .awaitResult(moshiDeserializerOf(GetTitlesRootDto::class.java))
    }

    override suspend fun searchTitlesByWord(query: String): Result<SearchTitleRootDto, FuelError> {
        return Fuel.get("/SearchTitle/k_956nmhwo/$query")
            .awaitResult(moshiDeserializerOf(SearchTitleRootDto::class.java))
    }
}