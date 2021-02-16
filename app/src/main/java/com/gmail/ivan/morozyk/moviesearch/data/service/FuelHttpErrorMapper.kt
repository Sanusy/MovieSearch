package com.gmail.ivan.morozyk.moviesearch.data.service

class FuelHttpErrorMapper : HttpErrorMapper {
    override fun mapErrorCode(errorCode: Int) = when (errorCode) {
        -1 -> HttpError.NoInternetError
        401 -> HttpError.NotAuthorizedError
        404 -> HttpError.NotFoundError
        500 -> HttpError.InternalServerError
        else -> HttpError.UnknownError
    }
}