package com.numero.itube.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.numero.itube.api.YoutubeApi
import com.numero.itube.api.request.ChannelVideoRequest
import com.numero.itube.api.request.RelativeRequest
import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.api.response.RelativeResponse
import com.numero.itube.api.response.Response
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.api.response.VideoResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy

class YoutubeRepository(private val youtubeApi: YoutubeApi) : IYoutubeRepository {

    override val isProgressLiveData: MutableLiveData<Boolean> = MutableLiveData()

    // ページング用
    private val cacheSearchVideoList: MutableList<SearchResponse.Video> = mutableListOf()
    private val cacheChannelVideoList: MutableList<SearchResponse.Video> = mutableListOf()

    override fun loadSearchResponse(request: SearchVideoRequest): LiveData<Response<VideoResponse>> {
        isProgressLiveData.postValue(true)
        val response = MutableLiveData<Response<VideoResponse>>()
        val stream = if (request.hasNextPageToken.not()) {
            youtubeApi.search(request.key, request.searchWord)
        } else {
            val token = checkNotNull(request.nextPageToken)
            youtubeApi.search(request.key, request.searchWord, nextPageToken = token)
        }
        stream.map {
            if (request.hasNextPageToken.not()) {
                cacheSearchVideoList.clear()
            }
            cacheSearchVideoList.addAll(it.items)
            val list = mutableListOf<SearchResponse.Video>().apply {
                addAll(cacheSearchVideoList)
            }
            VideoResponse(it.nextPageToken, list)
        }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            isProgressLiveData.postValue(false)
                            response.postValue(Response.Success(it))
                        },
                        onError = {
                            isProgressLiveData.postValue(false)
                            response.postValue(Response.Error(it))
                        }
                )
        return response
    }

    override fun loadRelative(request: RelativeRequest): LiveData<Response<RelativeResponse>> {
        isProgressLiveData.postValue(true)
        val response = MutableLiveData<Response<RelativeResponse>>()
        val streams = Observables.zip(
                youtubeApi.searchRelative(request.key, request.videoId),
                youtubeApi.channel(request.key, request.channelId),
                youtubeApi.videoDetail(request.key, request.videoId)
        ) { relativeResponse, channelResponse, detailResponse ->
            RelativeResponse(relativeResponse, channelResponse, detailResponse)
        }
        streams.observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            isProgressLiveData.postValue(false)
                            response.postValue(Response.Success(it))
                        },
                        onError = {
                            isProgressLiveData.postValue(false)
                            response.postValue(Response.Error(it))
                        }
                )
        return response
    }

    override fun loadChannelVideoResponse(request: ChannelVideoRequest): LiveData<Response<VideoResponse>> {
        isProgressLiveData.postValue(true)
        val response = MutableLiveData<Response<VideoResponse>>()
        val stream = if (request.hasNextPageToken.not()) {
            youtubeApi.searchChannelVideo(request.key, request.channelId)
        } else {
            val token = checkNotNull(request.nextPageToken)
            youtubeApi.searchChannelVideo(request.key, request.channelId, nextPageToken = token)
        }
        stream.map {
            if (request.hasNextPageToken.not()) {
                cacheChannelVideoList.clear()
            }
            cacheChannelVideoList.addAll(it.items)
            val list = mutableListOf<SearchResponse.Video>().apply {
                addAll(cacheChannelVideoList)
            }
            VideoResponse(it.nextPageToken, list)
        }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            isProgressLiveData.postValue(false)
                            response.postValue(Response.Success(it))
                        },
                        onError = {
                            isProgressLiveData.postValue(false)
                            response.postValue(Response.Error(it))
                        }
                )
        return response
    }
}