package com.numero.itube.api.response

sealed class Result<T> {

    data class Success<T>(val response: T) : Result<T>()

    data class Error<T>(val throwable: Throwable?) : Result<T>()
}