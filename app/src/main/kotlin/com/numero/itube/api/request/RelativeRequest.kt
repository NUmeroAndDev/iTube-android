package com.numero.itube.api.request

data class RelativeRequest(
        override val key: String,
        val videoId: String,
        val channelId: String
) : YoutubeApiRequest