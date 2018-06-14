package com.numero.itube.repository

import com.numero.itube.api.YoutubeApi
import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.api.response.ChannelDetailResponse
import com.numero.itube.api.response.ChannelResponse
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.api.response.VideoDetailResponse
import kotlinx.coroutines.experimental.Deferred

class YoutubeRepository(private val youtubeApi: YoutubeApi) : IYoutubeRepository {

    override fun search(request: SearchVideoRequest): Deferred<SearchResponse> {
        return if (request.hasNextPageToken.not()) {
            youtubeApi.search(request.key, request.searchWord)
        } else {
            val token = checkNotNull(request.nextPageToken)
            youtubeApi.search(request.key, request.searchWord, nextPageToken = token)
        }
    }

    override fun loadRelative(key: String, id: String): Deferred<SearchResponse> {
        return youtubeApi.searchRelative(key, id)
    }

    override fun loadDetail(key: String, id: String): Deferred<VideoDetailResponse> {
        return youtubeApi.videoDetail(key, id)
    }

    override fun loadChannel(key: String, id: String): Deferred<ChannelResponse> {
        return youtubeApi.channel(key, id)
    }

    override fun loadChannelDetail(key: String, id: String): Deferred<ChannelDetailResponse> {
        return youtubeApi.channelDetail(key, id)
    }

    override fun loadChannelVideo(key: String, id: String, nextPageToken: String?): Deferred<SearchResponse> {
        return if (nextPageToken == null) {
            youtubeApi.searchChannelVideo(key, id)
        } else {
            youtubeApi.searchChannelVideo(key, id, nextPageToken = nextPageToken)
        }
    }
}