package com.numero.itube.api.response

import java.lang.Exception

data class RelativeResponse(
        val searchResponse: SearchResponse,
        val channelResponse: ChannelResponse,
        val videoDetailResponse: VideoDetailResponse) {

    fun checkResponse() {
        if (searchResponse.items.isEmpty() or videoDetailResponse.items.isEmpty()) {
            throw Exception()
        }
    }
}