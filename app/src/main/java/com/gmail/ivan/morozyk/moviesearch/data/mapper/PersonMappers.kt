package com.gmail.ivan.morozyk.moviesearch.data.mapper

import com.gmail.ivan.morozyk.moviesearch.data.Person
import com.gmail.ivan.morozyk.moviesearch.data.PersonDto
import com.gmail.ivan.morozyk.moviesearch.data.Title
import com.gmail.ivan.morozyk.moviesearch.data.TitleDto
import java.text.SimpleDateFormat
import java.util.*
import java.util.Calendar.*

class PersonDtoMapper :
    BaseMapper<PersonDto, Person> {

    override fun map(input: PersonDto): Person {
        val name = if (input.name == null && input.title != null) input.title else input.name!!

        return Person(
            input.id,
            name,
            input.image,
            input.asCharacter,
            input.role, null,
            input.summary,
            input.birthDate,
            input.deathDate,
            input.awards,
            null
        )
    }
}

class PersonDtoWithTitlesMapper(private val titleMapper: BaseMapper<TitleDto, Title>) :
    BaseMapper<PersonDto, Person> {

    override fun map(input: PersonDto): Person {
        val name = if (input.name == null && input.title != null) input.title else input.name!!

        val parser = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val formatter = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())

        val birthDate = input.birthDate?.let {
            val parse = parser.parse(input.birthDate)
            formatter.format(parse!!)
        }

        val deathDate = input.deathDate?.let {
            val parse = parser.parse(input.deathDate)
            formatter.format(parse!!)
        }

        val age = if (input.birthDate != null && input.deathDate != null) {
            val birth = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).parse(birthDate!!)!!
            val death = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).parse(deathDate!!)!!

            getDiffYears(birth, death)
        } else if (input.birthDate != null) {
            val birth = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).parse(birthDate!!)!!
            getDiffYears(birth, Date())
        } else null

        val castMovies = input.castMovies?.map { titleMapper.map(it) }

        return Person(
            input.id,
            name,
            input.image,
            input.asCharacter,
            input.role,
            age,
            input.summary,
            birthDate,
            deathDate,
            input.awards,
            castMovies
        )
    }

    private fun getDiffYears(first: Date, last: Date): Int {
        val firstDate = getCalendar(first)
        val secondDate = getCalendar(last)
        var diff = secondDate[YEAR] - firstDate[YEAR]
        if (firstDate[MONTH] > secondDate[MONTH] ||
            firstDate[MONTH] == secondDate[MONTH] && firstDate[DATE] > secondDate[DATE]
        ) {
            diff--
        }
        return diff
    }

    private fun getCalendar(date: Date): Calendar {
        val cal = getInstance(Locale.US)
        cal.time = date
        return cal
    }
}