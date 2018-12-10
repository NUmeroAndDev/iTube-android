package com.numero.itube.repository

import com.numero.itube.api.YoutubeApi
import com.numero.itube.api.request.ChannelVideoRequest
import com.numero.itube.api.request.RelativeRequest
import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.api.response.*
import com.numero.itube.extension.toResult
import io.reactivex.Observable
import io.reactivex.rxkotlin.Observables

class YoutubeRepository(private val youtubeApi: YoutubeApi) : IYoutubeRepository {

    // ページング用
    private val cacheSearchVideoList: MutableList<SearchResponse.Video> = mutableListOf()
    private val cacheChannelVideoList: MutableList<SearchResponse.Video> = mutableListOf()

    override fun loadSearchResponse(request: SearchVideoRequest): Observable<Result<VideoResponse>> {
        val stream = if (request.hasNextPageToken) {
            val token = checkNotNull(request.nextPageToken)
            youtubeApi.search(request.key, request.searchWord, nextPageToken = token)
        } else {
            youtubeApi.search(request.key, request.searchWord)
        }
        return stream.map {
            if (request.hasNextPageToken.not()) {
                cacheSearchVideoList.clear()
            }
            cacheSearchVideoList.addAll(it.items)
            val list = mutableListOf<SearchResponse.Video>().apply {
                addAll(cacheSearchVideoList)
            }
            VideoResponse(it.nextPageToken, list, it.pageInfo.totalResults)
        }.toResult()
    }

    override fun loadRelative(request: RelativeRequest): Observable<RelativeResponse> {
        return Observables.zip(
                youtubeApi.searchRelative(request.key, request.videoId),
                youtubeApi.channel(request.key, request.channelId),
                youtubeApi.videoDetail(request.key, request.videoId)
        ) { relativeResponse, channelResponse, detailResponse ->
            RelativeResponse(relativeResponse, channelResponse, detailResponse).apply {
                checkResponse()
            }
        }
    }

    override fun loadChannelVideoResponse(request: ChannelVideoRequest): Observable<VideoResponse> {
        val stream = if (request.hasNextPageToken) {
            val token = checkNotNull(request.nextPageToken)
            youtubeApi.searchChannelVideo(request.key, request.channelId, nextPageToken = token)
        } else {
            youtubeApi.searchChannelVideo(request.key, request.channelId)
        }
        return stream.map {
            if (request.hasNextPageToken.not()) {
                cacheChannelVideoList.clear()
            }
            cacheChannelVideoList.addAll(it.items)
            val list = mutableListOf<SearchResponse.Video>().apply {
                addAll(cacheChannelVideoList)
            }
            VideoResponse(it.nextPageToken, list, it.pageInfo.totalResults)
        }
    }

    override fun loadChannelDetail(key: String, channelId: String): Observable<ChannelDetailResponse> {
        return youtubeApi.channelDetail(key, channelId)
    }
}