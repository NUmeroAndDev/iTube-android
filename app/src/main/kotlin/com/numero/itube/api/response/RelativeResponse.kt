package com.numero.itube.api.response

data class RelativeResponse(
        val searchResponse: SearchResponse,
        val channelResponse: ChannelResponse,
        val videoDetailResponse: VideoDetailResponse) {

    fun checkResponse(): RelativeResponse {
        if (searchResponse.items.isEmpty() or videoDetailResponse.items.isEmpty()) {
            throw Exception()
        }
        return this
    }
}