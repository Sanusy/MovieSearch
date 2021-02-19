package com.gmail.ivan.morozyk.moviesearch.data.mapper

import com.gmail.ivan.morozyk.moviesearch.mvvm.Resource

class FuelWithMVVMErrorMapper : BaseMapper<Int, Resource.HttpError<Any>> {
    override fun map(input: Int) = when (input) {
        -1 -> Resource.HttpError.NoInternetError<Any>()
        401 -> Resource.HttpError.NotAuthorizedError<Any>()
        404 -> Resource.HttpError.NotFoundError<Any>()
        500 -> Resource.HttpError.InternalServerError<Any>()
        else -> Resource.HttpError.UnknownError<Any>()
    }
}