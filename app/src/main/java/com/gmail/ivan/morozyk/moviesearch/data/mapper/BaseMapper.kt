package com.gmail.ivan.morozyk.moviesearch.data.mapper

interface BaseMapper<I, O> {

    fun map(input: I): O
}