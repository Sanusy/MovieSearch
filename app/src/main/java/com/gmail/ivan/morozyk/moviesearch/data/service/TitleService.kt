package com.gmail.ivan.morozyk.moviesearch.data.service

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result
import com.gmail.ivan.morozyk.moviesearch.data.GetTitlesRootDto
import com.gmail.ivan.morozyk.moviesearch.data.SearchTitleRootDto

interface TitleService {

    suspend fun getTitlesByQuery(query: String): Result<GetTitlesRootDto, FuelError>

    suspend fun searchTitlesByWord(query: String): Result<SearchTitleRootDto, FuelError>
}