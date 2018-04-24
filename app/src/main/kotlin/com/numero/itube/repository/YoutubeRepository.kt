package com.numero.itube.repository

import com.numero.itube.api.YoutubeApi
import com.numero.itube.api.response.SearchResponse
import kotlinx.coroutines.experimental.Deferred

class YoutubeRepository(private val youtubeApi: YoutubeApi) : IYoutubeRepository {

    override fun search(key: String, searchWord: String, nextPageToken: String?): Deferred<SearchResponse> {
        return if (nextPageToken == null) {
            youtubeApi.search(key, searchWord)
        } else {
            youtubeApi.search(key, searchWord, nextPageToken)
        }
    }
}