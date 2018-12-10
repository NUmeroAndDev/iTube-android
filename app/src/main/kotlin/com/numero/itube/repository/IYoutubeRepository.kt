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

    fun loadSearch(request: SearchVideoRequest): Result<VideoResponse>

    fun loadChannelVideo(request: ChannelVideoRequest): Observable<Result<VideoResponse>>

    fun loadChannelDetail(key: String, channelId: String): Observable<Result<ChannelDetailResponse>>
}