package com.numero.itube.model

data class VideoDetail(
        val videoId: VideoId,
        val title: String,
        val description: String,
        val thumbnailUrl: ThumbnailUrl,
        val relativeVideoList: List<Video.Search>,
        val channelDetail: ChannelDetail
)