package com.numero.itube.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.numero.itube.api.request.*
import com.numero.itube.api.response.*

interface IYoutubeRepository {

    val isProgressLiveData: MutableLiveData<Boolean>

    fun loadSearchResponse(request: SearchVideoRequest): LiveData<Response<SearchResponse>>

    fun loadRelativeResponse(request: RelativeVideoRequest): LiveData<Response<SearchResponse>>

    fun loadDetailResponse(request: VideoDetailRequest): LiveData<Response<VideoDetailResponse>>

    fun loadChannelResponse(request: ChannelRequest): LiveData<Response<ChannelResponse>>

    fun loadChannelDetailResponse(request: ChannelDetailRequest): LiveData<Response<ChannelDetailResponse>>

    fun loadChannelVideoResponse(request: ChannelVideoRequest): LiveData<Response<SearchResponse>>
}