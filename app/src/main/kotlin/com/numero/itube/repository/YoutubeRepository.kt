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
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.Observables
import io.reactivex.rxkotlin.subscribeBy

class YoutubeRepository(private val youtubeApi: YoutubeApi) : IYoutubeRepository {

    override val isProgressLiveData: MutableLiveData<Boolean> = MutableLiveData()

    override fun loadSearchResponse(request: SearchVideoRequest): LiveData<Response<SearchResponse>> {
        isProgressLiveData.postValue(true)
        val response = MutableLiveData<Response<SearchResponse>>()
        val stream = if (request.hasNextPageToken.not()) {
            youtubeApi.search(request.key, request.searchWord)
        } else {
            val token = checkNotNull(request.nextPageToken)
            youtubeApi.search(request.key, request.searchWord, nextPageToken = token)
        }
        stream.observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    isProgressLiveData.postValue(false)
                }
                .subscribeBy(
                        onNext = {
                            response.postValue(Response.Success(it))
                        },
                        onError = {
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
                .doOnNext {
                    isProgressLiveData.postValue(false)
                }
                .subscribeBy(
                        onNext = {
                            response.postValue(Response.Success(it))
                        },
                        onError = {
                            response.postValue(Response.Error(it))
                        }
                )
        return response
    }

    override fun loadChannelVideoResponse(request: ChannelVideoRequest): LiveData<Response<SearchResponse>> {
        isProgressLiveData.postValue(true)
        val response = MutableLiveData<Response<SearchResponse>>()
        val stream = if (request.hasNextPageToken.not()) {
            youtubeApi.searchChannelVideo(request.key, request.channelId)
        } else {
            val token = checkNotNull(request.nextPageToken)
            youtubeApi.searchChannelVideo(request.key, request.channelId, nextPageToken = token)
        }
        stream.observeOn(AndroidSchedulers.mainThread())
                .doOnNext {
                    isProgressLiveData.postValue(false)
                }
                .subscribeBy(
                        onNext = {
                            response.postValue(Response.Success(it))
                        },
                        onError = {
                            response.postValue(Response.Error(it))
                        }
                )
        return response
    }
}