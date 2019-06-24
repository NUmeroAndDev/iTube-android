package com.numero.itube.model

sealed class State<T> {

    data class Success<T>(val value: T) : State<T>()

    class Progress<T> : State<T>()

    data class Error<T>(val throwable: Throwable?) : State<T>()

    companion object {
        fun <T> progress(): State<T> = Progress()

        fun <T> success(value: T): State<T> = Success(value)

        fun <T> error(throwable: Throwable?): State<T> = Error(throwable)
    }
}