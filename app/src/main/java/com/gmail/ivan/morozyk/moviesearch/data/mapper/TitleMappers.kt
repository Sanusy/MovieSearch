package com.gmail.ivan.morozyk.moviesearch.data.mapper

import com.gmail.ivan.morozyk.moviesearch.data.Person
import com.gmail.ivan.morozyk.moviesearch.data.PersonDto
import com.gmail.ivan.morozyk.moviesearch.data.Title
import com.gmail.ivan.morozyk.moviesearch.data.TitleDto
import com.gmail.ivan.morozyk.moviesearch.data.room.TitleRoomDto
import java.text.SimpleDateFormat
import java.util.*

class TitleDtoMapper(private val personMapper: BaseMapper<PersonDto, Person>) :
    BaseMapper<TitleDto, Title> {

    override fun map(input: TitleDto): Title {
        var type = input.type
        var year = input.year
        val description = input.description
        val date = input.releaseDate?.let {
            val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

            val parse = parser.parse(input.releaseDate)
            formatter.format(parse!!)
        }

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

class TitleRoomDtoMapper : BaseMapper<TitleRoomDto, Title> {
    override fun map(input: TitleRoomDto) = with(input) {
        Title(
            id = id,
            name = name,
            type = type,
            year = year,
            image = image,
            rating = rating
        )
    }
}

class TitleMapper : BaseMapper<Pair<String, Title>, TitleRoomDto> {

    override fun map(input: Pair<String, Title>) = with(input.second) { TitleRoomDto(id, name, type, year, image, rating, input.first) }
}