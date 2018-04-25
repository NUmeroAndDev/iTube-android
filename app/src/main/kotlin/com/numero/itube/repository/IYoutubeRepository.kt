package com.numero.itube.repository

import com.numero.itube.api.response.SearchResponse
import com.numero.itube.api.response.VideoDetailResponse
import kotlinx.coroutines.experimental.Deferred

interface IYoutubeRepository {
    fun search(key: String, searchWord: String, nextPageToken: String? = null): Deferred<SearchResponse>

    fun loadRelative(key: String, id: String): Deferred<SearchResponse>

    fun loadDetail(key: String, id: String): Deferred<VideoDetailResponse>
}