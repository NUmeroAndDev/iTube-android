package com.numero.itube.repository

import com.numero.itube.api.request.RelativeRequest
import com.numero.itube.api.response.RelativeResponse
import com.numero.itube.api.response.Result

interface IYoutubeRepository {

    suspend fun loadRelative(request: RelativeRequest): Result<RelativeResponse>
}