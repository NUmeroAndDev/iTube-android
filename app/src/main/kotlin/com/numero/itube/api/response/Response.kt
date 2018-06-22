package com.numero.itube.api.response

sealed class Response<T>(open val response: T?, open val throwable: Throwable?) {
    class Success<T>(override val response: T) : Response<T>(response, null)
    class Error<T>(override val throwable: Throwable?) : Response<T>(null, throwable)
}