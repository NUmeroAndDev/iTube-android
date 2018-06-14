package com.numero.itube.api.request

data class VideoDetailRequest(
        override val key: String,
        val id: String
) : YoutubeApiRequest