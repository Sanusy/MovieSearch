package com.gmail.ivan.morozyk.moviesearch.data.mapper

import com.gmail.ivan.morozyk.moviesearch.data.Person
import com.gmail.ivan.morozyk.moviesearch.data.PersonDto

class PersonDtoMapper : BaseMapper<PersonDto, Person> {

    override fun map(input: PersonDto) = if (input.name == null && input.title != null) {
        Person(input.id, input.title, input.image, input.asCharacter)
    } else {
        Person(input.id, input.name!!, input.image, input.asCharacter)
    }
}