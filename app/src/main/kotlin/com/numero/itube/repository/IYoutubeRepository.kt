package com.numero.itube.repository

import com.numero.itube.api.request.*
import com.numero.itube.api.response.ChannelDetailResponse
import com.numero.itube.api.response.ChannelResponse
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.api.response.VideoDetailResponse
import kotlinx.coroutines.experimental.Deferred

interface IYoutubeRepository {
    fun search(request: SearchVideoRequest): Deferred<SearchResponse>

    fun loadRelative(request: RelativeVideoRequest): Deferred<SearchResponse>

    fun loadDetail(request: VideoDetailRequest): Deferred<VideoDetailResponse>

    fun loadChannel(request: ChannelRequest): Deferred<ChannelResponse>

    fun loadChannelDetail(request: ChannelDetailRequest): Deferred<ChannelDetailResponse>

    fun loadChannelVideo(request: ChannelVideoRequest): Deferred<SearchResponse>
}