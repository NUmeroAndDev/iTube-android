package com.numero.itube.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.numero.itube.api.YoutubeApi
import com.numero.itube.api.request.ChannelVideoRequest
import com.numero.itube.api.response.Result
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.extension.executeSync
import com.numero.itube.model.*
import kotlinx.coroutines.Dispatchers

class ChannelRepositoryImpl(
        private val youtubeApi: YoutubeApi
) : ChannelRepository {

    private val cacheChannelVideoList: MutableList<Video.Search> = mutableListOf()

    override fun fetchChannelDetail(key: String, channelId: String): LiveData<Result<ChannelDetail>> = liveData(Dispatchers.IO) {
        val result = youtubeApi.channelDetail(key, channelId).executeSync()
        when (result) {
            is Result.Error -> emit(Result.Error(result.throwable))
            is Result.Success -> {
                val detail = result.response.items[0]
                emit(Result.Success(
                        ChannelDetail(
                                ChannelId(detail.id),
                                detail.snippet.title,
                                ThumbnailUrl(detail.snippet.thumbnails.high.url),
                                BannerUrl(detail.branding.image.bannerTvMediumImageUrl)
                        )
                ))
            }
        }
    }

    override fun fetchChannelVideoList(request: ChannelVideoRequest): LiveData<Result<ChannelVideoList>> = liveData(Dispatchers.IO) {
        val call = if (request.hasNextPageToken) {
            val token = checkNotNull(request.nextPageToken)
            youtubeApi.searchChannelVideo(request.key, request.channelId, nextPageToken = token)
        } else {
            youtubeApi.searchChannelVideo(request.key, request.channelId)
        }
        val result = call.executeSync()
        when (result) {
            is Result.Error -> emit(Result.Error(result.throwable))
            is Result.Success -> {
                val response = result.response
                if (request.hasNextPageToken.not()) {
                    cacheChannelVideoList.clear()
                }
                cacheChannelVideoList.addAll(response.items.mapToVideoList())
                val list = mutableListOf<Video.Search>().apply {
                    addAll(cacheChannelVideoList)
                }
                emit(Result.Success(ChannelVideoList(response.nextPageToken, list, response.pageInfo.totalResults)))
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