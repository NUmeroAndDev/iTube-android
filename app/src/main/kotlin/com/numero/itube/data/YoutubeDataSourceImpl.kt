package com.numero.itube.data

import com.numero.itube.api.YoutubeApi
import com.numero.itube.api.response.ChannelDetailResponse
import com.numero.itube.api.response.Result
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.extension.executeSync
import com.numero.itube.model.ChannelId

class YoutubeDataSourceImpl(
        private val youtubeApi: YoutubeApi,
        private val apiKey: String
) : YoutubeDataSource {

    override suspend fun getVideos(searchWord: String, nextPageToken: String?): Result<SearchResponse> {
        val call = if (nextPageToken != null) {
            youtubeApi.search(apiKey, searchWord, nextPageToken = nextPageToken)
        } else {
            youtubeApi.search(apiKey, searchWord)
        }
        return call.executeSync()
    }

    override suspend fun getVideos(channelId: ChannelId, nextPageToken: String?): Result<SearchResponse> {
        val call = if (nextPageToken != null) {
            youtubeApi.searchChannelVideo(apiKey, channelId.value, nextPageToken = nextPageToken)
        } else {
            youtubeApi.searchChannelVideo(apiKey, channelId.value)
        }
        return call.executeSync()
    }

    override suspend fun getChannelDetail(channelId: ChannelId): Result<ChannelDetailResponse> {
        return youtubeApi.channelDetail(apiKey, channelId.value).executeSync()
    }
}