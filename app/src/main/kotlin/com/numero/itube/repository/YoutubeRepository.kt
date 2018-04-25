package com.numero.itube.repository

import com.numero.itube.api.YoutubeApi
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.api.response.VideoDetailResponse
import kotlinx.coroutines.experimental.Deferred

class YoutubeRepository(private val youtubeApi: YoutubeApi) : IYoutubeRepository {

    override fun search(key: String, searchWord: String, nextPageToken: String?): Deferred<SearchResponse> {
        return if (nextPageToken == null) {
            youtubeApi.search(key, searchWord)
        } else {
            youtubeApi.search(key, searchWord, nextPageToken)
        }
    }

    override fun loadRelative(key: String, id: String): Deferred<SearchResponse> {
        return youtubeApi.searchRelative(key, id)
    }

    override fun loadDetail(key: String, id: String): Deferred<VideoDetailResponse> {
        return youtubeApi.videoDetail(key, id)
    }
}