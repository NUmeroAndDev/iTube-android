package com.numero.itube.repository

import androidx.lifecycle.LiveData
import com.numero.itube.api.response.Result
import com.numero.itube.model.ChannelDetail

interface ChannelRepository {

    fun fetchChannelDetail(key: String, channelId: String): LiveData<Result<ChannelDetail>>

}