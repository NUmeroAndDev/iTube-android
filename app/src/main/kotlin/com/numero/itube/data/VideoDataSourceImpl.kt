package com.numero.itube.data

import com.numero.itube.api.YoutubeApi
import com.numero.itube.api.response.Result
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.extension.executeSync

class VideoDataSourceImpl(
        private val youtubeApi: YoutubeApi,
        private val apiKey: String
) : VideoDataSource {

    override suspend fun getVideos(searchWord: String, nextPageToken: String?): Result<SearchResponse> {
        val call = if (nextPageToken != null) {
            youtubeApi.search(apiKey, searchWord, nextPageToken = nextPageToken)
        } else {
            youtubeApi.search(apiKey, searchWord)
        }
        return call.executeSync()
    }
}