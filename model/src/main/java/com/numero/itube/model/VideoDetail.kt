package com.numero.itube.model

data class VideoDetail(
        val videoId: VideoId,
        val title: String,
        val description: String,
        val relativeVideoList: List<Video>,
        val channelDetail: ChannelDetail
)