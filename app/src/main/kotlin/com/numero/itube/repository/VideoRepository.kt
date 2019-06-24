package com.numero.itube.repository

import androidx.lifecycle.LiveData
import com.numero.itube.api.request.ChannelVideoRequest
import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.api.response.Result
import com.numero.itube.model.*

interface VideoRepository {

    fun fetchVideoDetail(videoId: VideoId, channelId: ChannelId): LiveData<Result<VideoDetail>>

    fun fetchVideoList(request: SearchVideoRequest): LiveData<Result<SearchVideoList>>

    fun fetchChannelVideoList(request: ChannelVideoRequest): LiveData<Result<ChannelVideoList>>

}