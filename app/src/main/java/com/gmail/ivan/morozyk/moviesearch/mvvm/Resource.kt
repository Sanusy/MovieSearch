package com.gmail.ivan.morozyk.moviesearch.mvvm

sealed class Resource<T>(val data: T? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Loading<T> : Resource<T>()
    sealed class HttpError<T> : Resource<T>() {
        class NotFoundError<T> : HttpError<T>()
        class NotAuthorizedError<T> : HttpError<T>()
        class NoInternetError<T> : HttpError<T>()
        class InternalServerError<T> : HttpError<T>()
        class UnknownError<T> : HttpError<T>()
    }
}