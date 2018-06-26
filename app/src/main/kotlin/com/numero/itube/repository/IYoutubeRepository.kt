package com.numero.itube.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.numero.itube.api.request.ChannelVideoRequest
import com.numero.itube.api.request.RelativeRequest
import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.api.response.RelativeResponse
import com.numero.itube.api.response.Response
import com.numero.itube.api.response.VideoResponse

interface IYoutubeRepository {

    val isProgressLiveData: MutableLiveData<Boolean>

    fun loadRelative(request: RelativeRequest): LiveData<Response<RelativeResponse>>

    fun loadSearchResponse(request: SearchVideoRequest): LiveData<Response<VideoResponse>>

    fun loadChannelVideoResponse(request: ChannelVideoRequest): LiveData<Response<VideoResponse>>
}