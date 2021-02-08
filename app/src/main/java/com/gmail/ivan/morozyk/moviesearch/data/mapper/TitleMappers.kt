package com.gmail.ivan.morozyk.moviesearch.data.mapper

import com.gmail.ivan.morozyk.moviesearch.data.Title
import com.gmail.ivan.morozyk.moviesearch.data.TitleDto

class TitleDtoMapper : BaseMapper<TitleDto, Title> {

    override fun map(input: TitleDto): Title {
        var type = input.type
        var year = input.year
        val description = input.description

        if (!description.isNullOrEmpty() && year.isNullOrEmpty()) {
            val descriptionItems = description.split('(', ')')
            type = when {
                descriptionItems.contains("Movie") -> "Movie"
                descriptionItems.contains("Short") -> "Short"
                descriptionItems.contains("TV Series") -> "TV Series"
                else -> type
            }

            val pattern = Regex("\\d{4}+")
            descriptionItems.forEach { if (it.contains(pattern)) year = it }
        }

        return Title(
            input.id,
            input.title,
            type,
            year,
            input.image,
            input.runtimeStr,
            input.plot,
            input.starList,
            input.genres,
            input.companies,
            input.imDbRating
        )
    }
}