package com.numero.itube.api.response

data class RelativeResponse(
        val searchResponse: SearchResponse,
        val channelResponse: ChannelResponse,
        val videoDetailResponse: VideoDetailResponse)