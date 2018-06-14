package com.numero.itube.repository

import com.numero.itube.api.request.ChannelRequest
import com.numero.itube.api.request.RelativeVideoRequest
import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.api.request.VideoDetailRequest
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

    fun loadChannelDetail(key: String, id: String): Deferred<ChannelDetailResponse>

    fun loadChannelVideo(key: String, id: String, nextPageToken: String? = null): Deferred<SearchResponse>
}