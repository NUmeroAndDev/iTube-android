package com.numero.itube.model

data class Channel(
        val id: ChannelId,
        val title: String
)

inline class ChannelId(val value: String)

data class ChannelDetail(
        val id: ChannelId,
        val title: String,
        val thumbnailUrl: ThumbnailUrl,
        val bannerUrl: BannerUrl
)

inline class BannerUrl(val value: String)