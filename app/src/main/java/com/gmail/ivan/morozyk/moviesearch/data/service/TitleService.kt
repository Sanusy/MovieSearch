package com.gmail.ivan.morozyk.moviesearch.data.service

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result
import com.gmail.ivan.morozyk.moviesearch.data.GetTitlesRootDto
import com.gmail.ivan.morozyk.moviesearch.data.SearchTitleRootDto
import com.gmail.ivan.morozyk.moviesearch.data.TitleDto

interface TitleService {

    suspend fun getTitlesByQuery(query: String): Result<GetTitlesRootDto, FuelError>

    suspend fun searchTitlesByWord(query: String): Result<SearchTitleRootDto, FuelError>

    suspend fun getTitleById(titleId: String): Result<TitleDto, FuelError>
}