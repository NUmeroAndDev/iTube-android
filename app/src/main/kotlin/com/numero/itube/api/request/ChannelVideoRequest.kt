package com.numero.itube.api.request

import com.numero.itube.model.ChannelId

data class ChannelVideoRequest(
        val channelId: ChannelId,
        val nextPageToken: String? = null
) {

    val hasNextPageToken: Boolean
        get() = nextPageToken.isNullOrEmpty().not()

}