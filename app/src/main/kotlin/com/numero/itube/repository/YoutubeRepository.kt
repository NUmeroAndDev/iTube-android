package com.numero.itube.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.numero.itube.api.YoutubeApi
import com.numero.itube.api.request.*
import com.numero.itube.api.response.*
import com.numero.itube.extension.execute

class YoutubeRepository(private val youtubeApi: YoutubeApi) : IYoutubeRepository {

    override fun loadSearchResponse(request: SearchVideoRequest): LiveData<Response<SearchResponse>> {
        val response = MutableLiveData<Response<SearchResponse>>()
        val call = if (request.hasNextPageToken.not()) {
            youtubeApi.search(request.key, request.searchWord)
        } else {
            val token = checkNotNull(request.nextPageToken)
            youtubeApi.search(request.key, request.searchWord, nextPageToken = token)
        }
        call.execute {
            response.postValue(it)
        }
        return response
    }

    override fun loadRelativeResponse(request: RelativeVideoRequest): LiveData<Response<SearchResponse>> {
        val response = MutableLiveData<Response<SearchResponse>>()
        youtubeApi.searchRelative(request.key, request.videoId).execute {
            response.postValue(it)
        }
        return response
    }

    override fun loadDetailResponse(request: VideoDetailRequest): LiveData<Response<VideoDetailResponse>> {
        val response = MutableLiveData<Response<VideoDetailResponse>>()
        youtubeApi.videoDetail(request.key, request.id).execute {
            response.postValue(it)
        }
        return response
    }

    override fun loadChannelResponse(request: ChannelRequest): LiveData<Response<ChannelResponse>> {
        val response = MutableLiveData<Response<ChannelResponse>>()
        youtubeApi.channel(request.key, request.id).execute {
            response.postValue(it)
        }
        return response
    }

    override fun loadChannelDetailResponse(request: ChannelDetailRequest): LiveData<Response<ChannelDetailResponse>> {
        val response = MutableLiveData<Response<ChannelDetailResponse>>()
        youtubeApi.channelDetail(request.key, request.id).execute {
            response.postValue(it)
        }
        return response
    }

    override fun loadChannelVideoResponse(request: ChannelVideoRequest): LiveData<Response<SearchResponse>> {
        val response = MutableLiveData<Response<SearchResponse>>()
        val call = if (request.hasNextPageToken.not()) {
            youtubeApi.searchChannelVideo(request.key, request.channelId)
        } else {
            val token = checkNotNull(request.nextPageToken)
            youtubeApi.searchChannelVideo(request.key, request.channelId, nextPageToken = token)
        }
        call.execute {
            response.postValue(it)
        }
        return response
    }
}