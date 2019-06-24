package com.numero.itube.repository

import com.numero.itube.api.YoutubeApi
import com.numero.itube.api.request.RelativeRequest
import com.numero.itube.api.response.RelativeResponse
import com.numero.itube.api.response.Result
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.extension.executeSync
import com.numero.itube.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

class YoutubeRepository(private val youtubeApi: YoutubeApi) : IYoutubeRepository {

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