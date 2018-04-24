package com.numero.itube.repository

import com.numero.itube.api.response.SearchResponse
import kotlinx.coroutines.experimental.Deferred

interface IYoutubeRepository {
    fun search(key: String, searchWord: String, nextPageToken: String? = null): Deferred<SearchResponse>
}