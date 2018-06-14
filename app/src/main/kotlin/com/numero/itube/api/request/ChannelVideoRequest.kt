package com.numero.itube.api.request

data class ChannelVideoRequest(
        override val key: String,
        val channelId: String,
        val nextPageToken: String? = null
) : YoutubeApiRequest {

    val hasNextPageToken: Boolean
        get() = nextPageToken.isNullOrEmpty().not()

}