package com.gmail.ivan.morozyk.moviesearch.data.service

import com.github.kittinunf.fuel.core.FuelError
import com.github.kittinunf.result.Result
import com.gmail.ivan.morozyk.moviesearch.data.PersonDto
import com.gmail.ivan.morozyk.moviesearch.data.SearchPersonRootDto

interface PersonService {

    suspend fun getPersonList(query: String): Result<SearchPersonRootDto, FuelError>

    suspend fun getPersonById(personId: String): Result<PersonDto, FuelError>
}