package com.numero.itube.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.numero.itube.api.YoutubeApi
import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.api.response.Result
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.extension.executeSync
import com.numero.itube.model.*
import kotlinx.coroutines.Dispatchers

class VideoRepositoryImpl(
        private val youtubeApi: YoutubeApi
) : VideoRepository {

    private val cacheSearchVideoList: MutableList<Video.Search> = mutableListOf()

    override fun fetchVideoList(request: SearchVideoRequest): LiveData<Result<SearchVideoList>> = liveData(Dispatchers.IO) {
        val call = if (request.hasNextPageToken) {
            val token = checkNotNull(request.nextPageToken)
            youtubeApi.search(request.key, request.searchWord, nextPageToken = token)
        } else {
            youtubeApi.search(request.key, request.searchWord)
        }
        val result = call.executeSync()
        when (result) {
            is Result.Error -> emit(Result.Error(result.throwable))
            is Result.Success -> {
                val response = result.response
                if (request.hasNextPageToken.not()) {
                    cacheSearchVideoList.clear()
                }
                cacheSearchVideoList.addAll(response.items.mapToVideoList())
                val list = mutableListOf<Video.Search>().apply {
                    addAll(cacheSearchVideoList)
                }
                emit(Result.Success(SearchVideoList(response.nextPageToken, list, response.pageInfo.totalResults)))
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