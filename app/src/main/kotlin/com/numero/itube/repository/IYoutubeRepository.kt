package com.numero.itube.repository

import com.numero.itube.api.response.ChannelDetailResponse
import com.numero.itube.api.response.ChannelResponse
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.api.response.VideoDetailResponse
import io.reactivex.Observable

interface IYoutubeRepository {
    fun search(key: String, searchWord: String, nextPageToken: String? = null): Observable<SearchResponse>

    fun loadRelative(key: String, id: String): Observable<SearchResponse>

    fun loadDetail(key: String, id: String): Observable<VideoDetailResponse>

    fun loadChannel(key: String, id: String): Observable<ChannelResponse>

    fun loadChannelDetail(key: String, id: String): Observable<ChannelDetailResponse>

    fun loadChannelVideo(key: String, id: String, nextPageToken: String? = null): Observable<SearchResponse>
}