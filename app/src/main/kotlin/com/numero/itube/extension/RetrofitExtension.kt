package com.numero.itube.extension

import com.numero.itube.api.response.Response
import retrofit2.Call
import retrofit2.Callback

fun <T> Call<T>.execute(onResponse: ((response: Response<T>) -> Unit)) {
    enqueue(object : Callback<T> {
        override fun onFailure(call: Call<T>?, t: Throwable?) {
            onResponse(Response.Error(t))
        }

        override fun onResponse(call: Call<T>?, response: retrofit2.Response<T>?) {
            if (response == null) {
                onResponse(Response.Error(null))
                return
            }
            val body = response.body()
            if (body == null) {
                onResponse(Response.Error(null))
                return
            }
            onResponse(Response.Success(body))
        }
    })
}