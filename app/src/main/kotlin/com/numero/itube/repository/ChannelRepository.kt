package com.numero.itube.repository

import androidx.lifecycle.LiveData
import com.numero.itube.api.request.ChannelVideoRequest
import com.numero.itube.api.response.Result
import com.numero.itube.api.response.VideoResponse
import com.numero.itube.model.ChannelDetail
import com.numero.itube.model.ChannelVideoList

interface ChannelRepository {

    fun fetchChannelDetail(key: String, channelId: String): LiveData<Result<ChannelDetail>>

    fun fetchChannelVideoList(request: ChannelVideoRequest): LiveData<Result<ChannelVideoList>>
}