package com.numero.itube.api.response

sealed class Response<T> {

    data class Success<T>(val response: T) : Response<T>()

    data class Error<T>(val throwable: Throwable?) : Response<T>()
}