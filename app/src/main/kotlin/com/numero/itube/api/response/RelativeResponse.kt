package com.numero.itube.api.response

import com.numero.itube.model.Video

data class RelativeResponse(
        val searchVideoList: List<Video.Search>,
        val channelResponse: ChannelResponse,
        val videoDetailResponse: VideoDetailResponse) {

    fun checkResponse(): RelativeResponse {
        if (searchVideoList.isEmpty() or videoDetailResponse.items.isEmpty()) {
            throw Exception()
        }
        return this
    }
}