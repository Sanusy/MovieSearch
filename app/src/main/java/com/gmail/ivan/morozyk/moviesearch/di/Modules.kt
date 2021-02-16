package com.gmail.ivan.morozyk.moviesearch.di

import com.gmail.ivan.morozyk.moviesearch.data.Person
import com.gmail.ivan.morozyk.moviesearch.data.PersonDto
import com.gmail.ivan.morozyk.moviesearch.data.Title
import com.gmail.ivan.morozyk.moviesearch.data.TitleDto
import com.gmail.ivan.morozyk.moviesearch.data.mapper.BaseMapper
import com.gmail.ivan.morozyk.moviesearch.data.mapper.PersonDtoMapper
import com.gmail.ivan.morozyk.moviesearch.data.mapper.PersonDtoWithTitlesMapper
import com.gmail.ivan.morozyk.moviesearch.data.mapper.TitleDtoMapper
import com.gmail.ivan.morozyk.moviesearch.data.service.*
import com.gmail.ivan.morozyk.moviesearch.mvp.contract.ThemeStorage
import com.gmail.ivan.morozyk.moviesearch.mvp.presenter.*
import org.koin.core.qualifier.named
import org.koin.dsl.module

val servicesModule = module {
    single<TitleService> { TitleServiceImpl() }
    single<PersonService> { PersonServiceImpl() }
}

val mappersModule = module {
    single<BaseMapper<TitleDto, Title>>(named(TITLE_DTO_MAPPER)) {
        TitleDtoMapper(
            get(
                named(
                    PERSON_DTO_MAPPER
                )
            )
        )
    }
    single<BaseMapper<PersonDto, Person>>(named(PERSON_DTO_MAPPER)) { PersonDtoMapper() }
    single<BaseMapper<PersonDto, Person>>(named(PERSON_WITH_TITLES_DTO_MAPPER)) {
        PersonDtoWithTitlesMapper(
            get(named(TITLE_DTO_MAPPER))
        )
    }
    single<HttpErrorMapper> { FuelHttpErrorMapper() }
}

val titleListModule = module {
    factory { TitleListPresenter(get(), get(named(TITLE_DTO_MAPPER)), get()) }
}

val titleDetailsModule = module {
    factory { TitleDetailsPresenter(get(), get(named(TITLE_DTO_MAPPER)), get()) }
}

val personListModule = module {
    factory { PersonListPresenter(get(), get(named(PERSON_DTO_MAPPER)), get()) }
}

val personDetailsModule = module {
    factory { PersonDetailsPresenter(get(), get(named(PERSON_WITH_TITLES_DTO_MAPPER)), get()) }
}

val settingsModule = module {
    factory { (themeStorage: ThemeStorage) -> SettingsPresenter(themeStorage) }
}

private const val TITLE_DTO_MAPPER = "Title Dto Mapper"
private const val PERSON_DTO_MAPPER = "Person Dto Mapper"
private const val PERSON_WITH_TITLES_DTO_MAPPER = "Person With titles Dto mapper"