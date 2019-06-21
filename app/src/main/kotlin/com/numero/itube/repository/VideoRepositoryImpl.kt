package com.numero.itube.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.numero.itube.api.request.ChannelVideoRequest
import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.api.response.Result
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.data.YoutubeDataSource
import com.numero.itube.model.*
import kotlinx.coroutines.Dispatchers

class VideoRepositoryImpl(
        private val videoDataSource: YoutubeDataSource
) : VideoRepository {

    private val cacheSearchVideoList: MutableList<Video.Search> = mutableListOf()
    private val cacheChannelVideoList: MutableList<Video.Search> = mutableListOf()

    override fun fetchVideoDetail(videoId: VideoId): LiveData<Result<VideoDetail>> = liveData(Dispatchers.IO) {
        val result = videoDataSource.getVideoDetail(videoId)
        val relative = videoDataSource.getVideos(videoId)
        if (result is Result.Success && relative is Result.Success) {
            val response = result.response
            val info = response.items[0]
            val videoDetail = VideoDetail(
                    VideoId(info.id),
                    info.snippet.title,
                    info.snippet.description,
                    relative.response.items.mapToVideoList(),
                    Channel(
                            ChannelId(info.snippet.channelId),
                            info.snippet.channelTitle
                    ))
            emit(Result.Success(videoDetail))
        } else {
            emit(Result.Error<VideoDetail>(Exception()))
        }
    }

    override fun fetchVideoList(request: SearchVideoRequest): LiveData<Result<SearchVideoList>> = liveData(Dispatchers.IO) {
        val result = videoDataSource.getVideos(request.searchWord, request.nextPageToken)
        when (result) {
            is Result.Error -> emit(Result.Error<SearchVideoList>(result.throwable))
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

    override fun fetchChannelVideoList(request: ChannelVideoRequest): LiveData<Result<ChannelVideoList>> = liveData(Dispatchers.IO) {
        val result = videoDataSource.getVideos(request.channelId, request.nextPageToken)
        when (result) {
            is Result.Error -> emit(Result.Error<ChannelVideoList>(result.throwable))
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