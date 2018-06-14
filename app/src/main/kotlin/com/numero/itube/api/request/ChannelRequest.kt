package com.numero.itube.api.request

data class ChannelRequest(
        override val key: String,
        val id: String
) : YoutubeApiRequest