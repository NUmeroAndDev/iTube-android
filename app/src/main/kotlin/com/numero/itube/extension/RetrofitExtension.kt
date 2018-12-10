package com.numero.itube.extension

import com.numero.itube.api.response.Result
import io.reactivex.Observable
import retrofit2.Call

fun <T> Call<T>.executeAsync(): Result<T> {
    val response = execute()
    return if (response.isSuccessful) {
        val body = response.body() ?: return Result.Error(Exception("No response"))
        return Result.Success(body)
    } else {
        Result.Error(Exception("status code : ${response.code()}"))
    }
}

@Deprecated("coroutinesに置き換えるまで")
fun <T> Observable<T>.toResult(): Observable<Result<T>> {
    return this.map<Result<T>> {
        Result.Success(it)
    }.onErrorResumeNext { t: Throwable ->
        Observable.just(Result.Error(t))
    }
}
