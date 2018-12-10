package com.numero.itube.repository

import com.numero.itube.api.YoutubeApi
import com.numero.itube.api.request.ChannelVideoRequest
import com.numero.itube.api.request.RelativeRequest
import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.api.response.*
import com.numero.itube.extension.executeAsync
import com.numero.itube.extension.toResult
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables

class YoutubeRepository(private val youtubeApi: YoutubeApi) : IYoutubeRepository {

    // ページング用
    private val cacheSearchVideoList: MutableList<SearchResponse.Video> = mutableListOf()
    private val cacheChannelVideoList: MutableList<SearchResponse.Video> = mutableListOf()

    override fun loadSearch(request: SearchVideoRequest): Result<VideoResponse> {
        val call = if (request.hasNextPageToken) {
            val token = checkNotNull(request.nextPageToken)
            youtubeApi.search(request.key, request.searchWord, nextPageToken = token)
        } else {
            youtubeApi.search(request.key, request.searchWord)
        }
        val result = call.executeAsync()
        return when (result) {
            is Result.Error -> Result.Error(result.throwable)
            is Result.Success -> {
                val response = result.response
                if (request.hasNextPageToken.not()) {
                    cacheSearchVideoList.clear()
                }
                cacheSearchVideoList.addAll(response.items)
                val list = mutableListOf<SearchResponse.Video>().apply {
                    addAll(cacheSearchVideoList)
                }
                Result.Success(VideoResponse(response.nextPageToken, list, response.pageInfo.totalResults))
            }
        }
    }

    override fun loadRelative(request: RelativeRequest): Observable<Result<RelativeResponse>> {
        return Observables.zip(
                youtubeApi.searchRelative(request.key, request.videoId),
                youtubeApi.channel(request.key, request.channelId),
                youtubeApi.videoDetail(request.key, request.videoId)
        ) { relativeResponse, channelResponse, detailResponse ->
            RelativeResponse(relativeResponse, channelResponse, detailResponse).apply {
                checkResponse()
            }
        }.toResult()
    }

    override fun loadChannelVideo(request: ChannelVideoRequest): Result<VideoResponse> {
        val call = if (request.hasNextPageToken) {
            val token = checkNotNull(request.nextPageToken)
            youtubeApi.searchChannelVideo(request.key, request.channelId, nextPageToken = token)
        } else {
            youtubeApi.searchChannelVideo(request.key, request.channelId)
        }
        val result = call.executeAsync()
        return when (result) {
            is Result.Error -> Result.Error(result.throwable)
            is Result.Success -> {
                val response = result.response
                if (request.hasNextPageToken.not()) {
                    cacheChannelVideoList.clear()
                }
                cacheChannelVideoList.addAll(response.items)
                val list = mutableListOf<SearchResponse.Video>().apply {
                    addAll(cacheChannelVideoList)
                }
                Result.Success(VideoResponse(response.nextPageToken, list, response.pageInfo.totalResults))
            }
        }
    }

    override fun loadChannelDetail(key: String, channelId: String): Result<ChannelDetailResponse> {
        return youtubeApi.channelDetail(key, channelId).executeAsync()
    }
}