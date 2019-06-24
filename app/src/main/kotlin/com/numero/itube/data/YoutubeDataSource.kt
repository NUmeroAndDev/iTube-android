package com.numero.itube.data

import com.numero.itube.api.response.ChannelDetailResponse
import com.numero.itube.api.response.Result
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.api.response.VideoDetailResponse
import com.numero.itube.model.ChannelId
import com.numero.itube.model.VideoId

interface YoutubeDataSource {
    suspend fun getVideos(searchWord: String, nextPageToken: String? = null): Result<SearchResponse>

    suspend fun getVideos(channelId: ChannelId, nextPageToken: String? = null): Result<SearchResponse>

    suspend fun getVideos(videoId: VideoId): Result<SearchResponse>

    suspend fun getVideoDetail(videoId: VideoId): Result<VideoDetailResponse>

    suspend fun getChannelDetail(channelId: ChannelId): Result<ChannelDetailResponse>
}