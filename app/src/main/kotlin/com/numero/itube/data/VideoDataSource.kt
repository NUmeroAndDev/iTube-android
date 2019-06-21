package com.numero.itube.data

import com.numero.itube.api.response.Result
import com.numero.itube.api.response.SearchResponse

interface VideoDataSource {
    suspend fun getVideos(searchWord: String, nextPageToken: String? = null): Result<SearchResponse>
}