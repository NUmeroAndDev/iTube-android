package com.numero.itube.api.request

data class ChannelDetailRequest(
        override val key: String,
        val id: String
) : YoutubeApiRequest