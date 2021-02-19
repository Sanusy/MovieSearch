package com.gmail.ivan.morozyk.moviesearch.data.mapper

class FuelHttpErrorMapper : BaseMapper<Int, HttpError> {
    override fun map(input: Int) = when (input) {
        -1 -> HttpError.NoInternetError
        401 -> HttpError.NotAuthorizedError
        404 -> HttpError.NotFoundError
        500 -> HttpError.InternalServerError
        else -> HttpError.UnknownError
    }
}

sealed class HttpError {
    object NotFoundError : HttpError()
    object NotAuthorizedError : HttpError()
    object NoInternetError : HttpError()
    object InternalServerError : HttpError()
    object UnknownError : HttpError()
}