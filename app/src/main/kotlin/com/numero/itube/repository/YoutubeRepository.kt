package com.numero.itube.repository

import com.numero.itube.api.YoutubeApi
import com.numero.itube.api.request.ChannelVideoRequest
import com.numero.itube.api.request.RelativeRequest
import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.api.response.*
import com.numero.itube.extension.executeSync
import com.numero.itube.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class YoutubeRepository(private val youtubeApi: YoutubeApi) : IYoutubeRepository {

    // ページング用
    private val cacheSearchVideoList: MutableList<Video.Search> = mutableListOf()
    private val cacheChannelVideoList: MutableList<Video.Search> = mutableListOf()

    override suspend fun loadSearch(request: SearchVideoRequest): Result<VideoResponse> {
        val call = if (request.hasNextPageToken) {
            val token = checkNotNull(request.nextPageToken)
            youtubeApi.search(request.key, request.searchWord, nextPageToken = token)
        } else {
            youtubeApi.search(request.key, request.searchWord)
        }
        val result = call.executeSync()
        return when (result) {
            is Result.Error -> Result.Error(result.throwable)
            is Result.Success -> {
                val response = result.response
                if (request.hasNextPageToken.not()) {
                    cacheSearchVideoList.clear()
                }
                cacheSearchVideoList.addAll(response.items.mapToVideoList())
                val list = mutableListOf<Video.Search>().apply {
                    addAll(cacheSearchVideoList)
                }
                Result.Success(VideoResponse(response.nextPageToken, list, response.pageInfo.totalResults))
            }
        }
    }

    override suspend fun loadRelative(request: RelativeRequest): Result<RelativeResponse> = withContext(Dispatchers.Default) {
        val searchRelativeResult = async { youtubeApi.searchRelative(request.key, request.videoId).executeSync() }.await()
        val channelDetailResult = async { youtubeApi.channel(request.key, request.channelId).executeSync() }.await()
        val videoDetailResult = async { youtubeApi.videoDetail(request.key, request.videoId).executeSync() }.await()
        if (searchRelativeResult is Result.Success && channelDetailResult is Result.Success && videoDetailResult is Result.Success) {
            val response = RelativeResponse(searchRelativeResult.response.items.mapToVideoList(), channelDetailResult.response, videoDetailResult.response)
            try {
                Result.Success(response.checkResponse())
            } catch (t: Throwable) {
                Result.Error<RelativeResponse>(t)
            }
        } else {
            Result.Error(null)
        }
    }

    override suspend fun loadChannelVideo(request: ChannelVideoRequest): Result<VideoResponse> {
        val call = if (request.hasNextPageToken) {
            val token = checkNotNull(request.nextPageToken)
            youtubeApi.searchChannelVideo(request.key, request.channelId, nextPageToken = token)
        } else {
            youtubeApi.searchChannelVideo(request.key, request.channelId)
        }
        val result = call.executeSync()
        return when (result) {
            is Result.Error -> Result.Error(result.throwable)
            is Result.Success -> {
                val response = result.response
                if (request.hasNextPageToken.not()) {
                    cacheChannelVideoList.clear()
                }
                cacheChannelVideoList.addAll(response.items.mapToVideoList())
                val list = mutableListOf<Video.Search>().apply {
                    addAll(cacheChannelVideoList)
                }
                Result.Success(VideoResponse(response.nextPageToken, list, response.pageInfo.totalResults))
            }
        }
    }

    override suspend fun loadChannelDetail(key: String, channelId: String): Result<ChannelDetailResponse> {
        return youtubeApi.channelDetail(key, channelId).executeSync()
    }

    private fun List<SearchResponse.Video>.mapToVideoList(): List<Video.Search> {
        return map {
            Video.Search(
                    VideoId(it.id.videoId),
                    ThumbnailUrl(it.snippet.thumbnails.high.url),
                    it.snippet.title,
                    Channel(
                            ChannelId(it.snippet.channelId),
                            it.snippet.channelTitle
                    )
            )
        }
    }
}