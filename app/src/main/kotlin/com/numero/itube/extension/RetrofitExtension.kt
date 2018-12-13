package com.numero.itube.extension

import com.numero.itube.api.response.Result
import retrofit2.Call

fun <T> Call<T>.executeSync(): Result<T> {
    return try {
        val response = execute()
        return if (response.isSuccessful) {
            val body = response.body() ?: return Result.Error(Exception("No response"))
            return Result.Success(body)
        } else {
            Result.Error(Exception("status code : ${response.code()}"))
        }
    } catch (t: Throwable) {
        Result.Error(t)
    }
}