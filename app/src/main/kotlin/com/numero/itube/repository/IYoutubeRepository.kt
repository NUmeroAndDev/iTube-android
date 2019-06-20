package com.numero.itube.repository

import com.numero.itube.api.request.ChannelVideoRequest
import com.numero.itube.api.request.RelativeRequest
import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.api.response.RelativeResponse
import com.numero.itube.api.response.Result
import com.numero.itube.api.response.VideoResponse
import com.numero.itube.model.ChannelDetail
import com.numero.itube.model.SearchVideoList

interface IYoutubeRepository {

    suspend fun loadRelative(request: RelativeRequest): Result<RelativeResponse>

    suspend fun loadSearch(request: SearchVideoRequest): Result<SearchVideoList>

    suspend fun loadChannelVideo(request: ChannelVideoRequest): Result<VideoResponse>

    suspend fun loadChannelDetail(key: String, channelId: String): Result<ChannelDetail>
}