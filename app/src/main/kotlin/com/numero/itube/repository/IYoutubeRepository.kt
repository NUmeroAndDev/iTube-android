package com.numero.itube.repository

import com.numero.itube.api.request.RelativeVideoRequest
import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.api.response.ChannelDetailResponse
import com.numero.itube.api.response.ChannelResponse
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.api.response.VideoDetailResponse
import kotlinx.coroutines.experimental.Deferred

interface IYoutubeRepository {
    fun search(request: SearchVideoRequest): Deferred<SearchResponse>

    fun loadRelative(request: RelativeVideoRequest): Deferred<SearchResponse>

    fun loadDetail(key: String, id: String): Deferred<VideoDetailResponse>

    fun loadChannel(key: String, id: String): Deferred<ChannelResponse>

    fun loadChannelDetail(key: String, id: String): Deferred<ChannelDetailResponse>

    fun loadChannelVideo(key: String, id: String, nextPageToken: String? = null): Deferred<SearchResponse>
}