package com.numero.itube.repository

import androidx.lifecycle.LiveData
import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.api.response.Result
import com.numero.itube.model.SearchVideoList

interface VideoRepository {

    fun fetchVideoList(request: SearchVideoRequest): LiveData<Result<SearchVideoList>>

}