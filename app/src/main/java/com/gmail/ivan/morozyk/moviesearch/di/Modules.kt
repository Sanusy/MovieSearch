package com.gmail.ivan.morozyk.moviesearch.di

import androidx.room.Room
import com.gmail.ivan.morozyk.moviesearch.SharedPrefHelper
import com.gmail.ivan.morozyk.moviesearch.data.Person
import com.gmail.ivan.morozyk.moviesearch.data.PersonDto
import com.gmail.ivan.morozyk.moviesearch.data.Title
import com.gmail.ivan.morozyk.moviesearch.data.TitleDto
import com.gmail.ivan.morozyk.moviesearch.data.mapper.*
import com.gmail.ivan.morozyk.moviesearch.data.room.MoviesDatabase
import com.gmail.ivan.morozyk.moviesearch.data.room.TitleRoomDto
import com.gmail.ivan.morozyk.moviesearch.data.service.PersonService
import com.gmail.ivan.morozyk.moviesearch.data.service.PersonServiceImpl
import com.gmail.ivan.morozyk.moviesearch.data.service.TitleService
import com.gmail.ivan.morozyk.moviesearch.data.service.TitleServiceImpl
import com.gmail.ivan.morozyk.moviesearch.mvp.presenter.PersonDetailsPresenter
import com.gmail.ivan.morozyk.moviesearch.mvp.presenter.PersonListPresenter
import com.gmail.ivan.morozyk.moviesearch.mvp.presenter.SettingsPresenter
import com.gmail.ivan.morozyk.moviesearch.mvp.presenter.TitleDetailsPresenter
import com.gmail.ivan.morozyk.moviesearch.mvvm.Resource
import com.gmail.ivan.morozyk.moviesearch.mvvm.viewmodel.TitleListViewModel
import com.gmail.ivan.morozyk.moviesearch.navigation.Router
import com.gmail.ivan.morozyk.moviesearch.navigation.RouterImpl
import org.koin.android.ext.koin.androidContext
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val navigationModule = module {
    single<Router> { RouterImpl() }
}

val localStorageModule = module {
    single { SharedPrefHelper(androidContext()) }
}

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
    single<BaseMapper<Int, HttpError>>(named(ERROR_MAPPER)) { FuelHttpErrorMapper() }
    single<BaseMapper<Int, Resource.HttpError<Any>>>(named(ERROR_MVVM_MAPPER)) { FuelWithMVVMErrorMapper() }
    single<BaseMapper<Pair<String,Title>, TitleRoomDto>>(named(TITLE_TO_ROOM_TITLE_MAPPER)) { TitleMapper() }
    single<BaseMapper<TitleRoomDto, Title>>(named(ROOM_TITLE_TO_TITLE_MAPPER)) { TitleRoomDtoMapper() }
}

val titleListModule = module {
    viewModel {
        TitleListViewModel(
            get(),
            get(),
            get(named(TITLE_TO_ROOM_TITLE_MAPPER)),
            get(named(ROOM_TITLE_TO_TITLE_MAPPER)),
            get(named(TITLE_DTO_MAPPER)),
            get(named(ERROR_MVVM_MAPPER)),
            get()
        )
    }
}

val titleDetailsModule = module {
    factory {
        TitleDetailsPresenter(
            get(),
            get(named(TITLE_DTO_MAPPER)),
            get(named(ERROR_MAPPER)),
            get()
        )
    }
}

val personListModule = module {
    factory {
        PersonListPresenter(
            get(),
            get(named(PERSON_DTO_MAPPER)),
            get(named(ERROR_MAPPER)),
            get()
        )
    }
}

val personDetailsModule = module {
    factory {
        PersonDetailsPresenter(
            get(),
            get(named(PERSON_WITH_TITLES_DTO_MAPPER)),
            get(named(ERROR_MAPPER)), get()
        )
    }
}

val settingsModule = module {
    factory { SettingsPresenter(get()) }
}

val dataBaseModule = module {
    single { Room.databaseBuilder(get(), MoviesDatabase::class.java, "movie-search").build() }
    single { get<MoviesDatabase>().titleDao() }
}

private const val TITLE_DTO_MAPPER = "Title Dto Mapper"
private const val PERSON_DTO_MAPPER = "Person Dto Mapper"
private const val PERSON_WITH_TITLES_DTO_MAPPER = "Person With titles Dto mapper"
private const val ERROR_MAPPER = "error mapper"
private const val ERROR_MVVM_MAPPER = "error mvvm mapper"
private const val TITLE_TO_ROOM_TITLE_MAPPER = "Title to TitleRoomDto mapper"
private const val ROOM_TITLE_TO_TITLE_MAPPER = "TitleRoomDto to Title mapper"