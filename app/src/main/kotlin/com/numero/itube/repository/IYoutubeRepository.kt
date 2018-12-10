package com.numero.itube.repository

import com.numero.itube.api.request.ChannelVideoRequest
import com.numero.itube.api.request.RelativeRequest
import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.api.response.ChannelDetailResponse
import com.numero.itube.api.response.RelativeResponse
import com.numero.itube.api.response.Result
import com.numero.itube.api.response.VideoResponse
import io.reactivex.Observable

interface IYoutubeRepository {

    fun loadRelative(request: RelativeRequest): Observable<Result<RelativeResponse>>

    fun loadSearchResponse(request: SearchVideoRequest): Observable<Result<VideoResponse>>

    fun loadChannelVideoResponse(request: ChannelVideoRequest): Observable<VideoResponse>

    fun loadChannelDetail(key: String, channelId: String): Observable<ChannelDetailResponse>
}