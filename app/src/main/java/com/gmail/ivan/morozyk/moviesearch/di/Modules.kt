package com.gmail.ivan.morozyk.moviesearch.di

import com.gmail.ivan.morozyk.moviesearch.data.Person
import com.gmail.ivan.morozyk.moviesearch.data.PersonDto
import com.gmail.ivan.morozyk.moviesearch.data.Title
import com.gmail.ivan.morozyk.moviesearch.data.TitleDto
import com.gmail.ivan.morozyk.moviesearch.data.mapper.BaseMapper
import com.gmail.ivan.morozyk.moviesearch.data.mapper.PersonDtoMapper
import com.gmail.ivan.morozyk.moviesearch.data.mapper.TitleDtoMapper
import com.gmail.ivan.morozyk.moviesearch.data.service.PersonService
import com.gmail.ivan.morozyk.moviesearch.data.service.PersonServiceImpl
import com.gmail.ivan.morozyk.moviesearch.data.service.TitleService
import com.gmail.ivan.morozyk.moviesearch.data.service.TitleServiceImpl
import com.gmail.ivan.morozyk.moviesearch.mvp.presenter.PersonListPresenter
import com.gmail.ivan.morozyk.moviesearch.mvp.presenter.TitleDetailsPresenter
import com.gmail.ivan.morozyk.moviesearch.mvp.presenter.TitleListPresenter
import org.koin.core.qualifier.named
import org.koin.dsl.module

val servicesModule = module {
    single<TitleService> { TitleServiceImpl() }
    single<PersonService>{ PersonServiceImpl() }
}

val mappersModule = module {
    single<BaseMapper<TitleDto, Title>>(named(TITLE_DTO_MAPPER)) { TitleDtoMapper(get(named(PERSON_DTO_MAPPER))) }
    single<BaseMapper<PersonDto, Person>>(named(PERSON_DTO_MAPPER)) { PersonDtoMapper() }
}

val presentersModule = module {
    factory { TitleListPresenter(get(), get(named(TITLE_DTO_MAPPER))) }
    factory { TitleDetailsPresenter(get(), get(named(TITLE_DTO_MAPPER))) }
    factory { PersonListPresenter(get(), get(named(PERSON_DTO_MAPPER))) }
}

private const val TITLE_DTO_MAPPER = "Title Dto Mapper"
private const val PERSON_DTO_MAPPER = "Person Dto Mapper"