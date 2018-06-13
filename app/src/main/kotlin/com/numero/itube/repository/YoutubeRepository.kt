package com.numero.itube.repository

import com.numero.itube.api.YoutubeApi
import com.numero.itube.api.response.ChannelDetailResponse
import com.numero.itube.api.response.ChannelResponse
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.api.response.VideoDetailResponse
import io.reactivex.Observable
import io.reactivex.Single

class YoutubeRepository(private val youtubeApi: YoutubeApi) : IYoutubeRepository {

    override fun search(key: String, searchWord: String, nextPageToken: String?): Observable<SearchResponse> {
        return if (nextPageToken == null) {
            youtubeApi.search(key, searchWord)
        } else {
            youtubeApi.search(key, searchWord, nextPageToken = nextPageToken)
        }
    }

    override fun loadRelative(key: String, id: String): Observable<SearchResponse> {
        return youtubeApi.searchRelative(key, id)
    }

    override fun loadDetail(key: String, id: String): Observable<VideoDetailResponse> {
        return youtubeApi.videoDetail(key, id)
    }

    override fun loadChannel(key: String, id: String): Observable<ChannelResponse> {
        return youtubeApi.channel(key, id)
    }

    override fun loadChannelDetail(key: String, id: String): Observable<ChannelDetailResponse> {
        return youtubeApi.channelDetail(key, id)
    }

    override fun loadChannelVideo(key: String, id: String, nextPageToken: String?): Observable<SearchResponse> {
        return if (nextPageToken == null) {
            youtubeApi.searchChannelVideo(key, id)
        } else {
            youtubeApi.searchChannelVideo(key, id, nextPageToken = nextPageToken)
        }
    }
}