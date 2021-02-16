package com.gmail.ivan.morozyk.moviesearch.data.service

interface HttpErrorMapper {

    fun mapErrorCode(errorCode: Int): HttpError
}

sealed class HttpError {
    object NotFoundError : HttpError()
    object NotAuthorizedError : HttpError()
    object NoInternetError : HttpError()
    object InternalServerError : HttpError()
    object UnknownError : HttpError()
}