package com.numero.itube.repository

import com.numero.itube.api.YoutubeApi
import com.numero.itube.api.request.ChannelVideoRequest
import com.numero.itube.api.request.RelativeRequest
import com.numero.itube.api.response.RelativeResponse
import com.numero.itube.api.response.Result
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.api.response.VideoResponse
import com.numero.itube.extension.executeSync
import com.numero.itube.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class YoutubeRepository(private val youtubeApi: YoutubeApi) : IYoutubeRepository {

    // ページング用
    private val cacheChannelVideoList: MutableList<Video.Search> = mutableListOf()

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

    override suspend fun loadChannelDetail(key: String, channelId: String): Result<ChannelDetail> {
        val result = youtubeApi.channelDetail(key, channelId).executeSync()
        return when (result) {
            is Result.Error -> Result.Error(result.throwable)
            is Result.Success -> {
                val response = result.response
                val detail = response.items[0]
                Result.Success(ChannelDetail(
                        ChannelId(detail.id),
                        detail.snippet.title,
                        ThumbnailUrl(detail.snippet.thumbnails.high.url),
                        BannerUrl(detail.branding.image.bannerTvMediumImageUrl)
                ))
            }
        }
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