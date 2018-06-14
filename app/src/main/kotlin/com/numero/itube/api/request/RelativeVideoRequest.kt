package com.numero.itube.api.request

data class RelativeVideoRequest(
        override val key: String,
        val videoId: String
) : YoutubeApiRequest