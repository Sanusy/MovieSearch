package com.gmail.ivan.morozyk.moviesearch.data.mapper

import com.gmail.ivan.morozyk.moviesearch.data.Title
import com.gmail.ivan.morozyk.moviesearch.data.TitleDto
import java.text.SimpleDateFormat
import java.util.*

class TitleDtoMapper : BaseMapper<TitleDto, Title> {

    override fun map(input: TitleDto): Title {
        var type = input.type
        var year = input.year
        val description = input.description
        var date = input.releaseDate

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

        date = date?.let {
            val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            val parse = parser.parse(date!!)
            formatter.format(parse!!)
        }

        val personMapper = PersonDtoMapper()
        val actorList = input.actorList?.map { personMapper.map(it) }

        return Title(
            input.id,
            input.title,
            type,
            year,
            input.image,
            input.runtimeStr,
            input.plot,
            date,
            actorList,
            input.genres,
            input.companies,
            input.imDbRating
        )
    }
}