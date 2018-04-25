package com.numero.itube.api

import com.numero.itube.api.response.SearchResponse
import com.numero.itube.api.response.VideoDetailResponse
import kotlinx.coroutines.experimental.Deferred
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeApi {
    @GET("search")
    fun search(
            @Query("key") key: String,
            @Query("q") searchWord: String,
            @Query("pageToken") nextPageToken: String,
            @Query("type") type: String = "video",
            @Query("part") part: String = "id,snippet",
            @Query("maxResults") maxResult: Int = 30
    ): Deferred<SearchResponse>

    @GET("search")
    fun search(
            @Query("key") key: String,
            @Query("q") searchWord: String,
            @Query("type") type: String = "video",
            @Query("part") part: String = "id,snippet",
            @Query("maxResults") maxResult: Int = 30
    ): Deferred<SearchResponse>

    @GET("videos")
    fun videoDetail(
            @Query("key") key: String,
            @Query("id") id: String,
            @Query("part") part: String = "id,snippet"
    ): Deferred<VideoDetailResponse>
}