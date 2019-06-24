package com.numero.itube.repository

import androidx.lifecycle.LiveData
import com.numero.itube.api.response.Result
import com.numero.itube.model.ChannelDetail
import com.numero.itube.model.ChannelId

interface ChannelRepository {

    fun fetchChannelDetail(channelId: ChannelId): LiveData<Result<ChannelDetail>>
}