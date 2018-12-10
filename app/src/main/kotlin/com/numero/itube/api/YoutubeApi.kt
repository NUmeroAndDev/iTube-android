package com.numero.itube.api

import com.numero.itube.api.response.ChannelDetailResponse
import com.numero.itube.api.response.ChannelResponse
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.api.response.VideoDetailResponse
import io.reactivex.Observable
import retrofit2.Call
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
    ): Call<SearchResponse>

    @GET("search")
    fun search(
            @Query("key") key: String,
            @Query("q") searchWord: String,
            @Query("type") type: String = "video",
            @Query("part") part: String = "id,snippet",
            @Query("maxResults") maxResult: Int = 30
    ): Call<SearchResponse>

    @GET("search")
    fun searchChannelVideo(
            @Query("key") key: String,
            @Query("channelId") channelId: String,
            @Query("type") type: String = "video",
            @Query("part") part: String = "id,snippet",
            @Query("maxResults") maxResult: Int = 30
    ): Observable<SearchResponse>

    @GET("search")
    fun searchChannelVideo(
            @Query("key") key: String,
            @Query("channelId") channelId: String,
            @Query("pageToken") nextPageToken: String,
            @Query("type") type: String = "video",
            @Query("part") part: String = "id,snippet",
            @Query("maxResults") maxResult: Int = 30
    ): Observable<SearchResponse>

    @GET("search")
    fun searchRelative(
            @Query("key") key: String,
            @Query("relatedToVideoId") id: String,
            @Query("type") type: String = "video",
            @Query("part") part: String = "id,snippet",
            @Query("maxResults") maxResult: Int = 30
    ): Observable<SearchResponse>

    @GET("videos")
    fun videoDetail(
            @Query("key") key: String,
            @Query("id") id: String,
            @Query("part") part: String = "id,snippet"
    ): Observable<VideoDetailResponse>

    @GET("channels")
    fun channel(
            @Query("key") key: String,
            @Query("id") id: String,
            @Query("part") part: String = "id,snippet"
    ): Observable<ChannelResponse>

    @GET("channels")
    fun channelDetail(
            @Query("key") key: String,
            @Query("id") id: String,
            @Query("part") part: String = "id,snippet,brandingSettings"
    ): Observable<ChannelDetailResponse>
}