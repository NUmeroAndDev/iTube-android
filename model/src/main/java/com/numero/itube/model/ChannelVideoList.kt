package com.numero.itube.model

import java.io.Serializable

data class ChannelVideoList(
        val nextPageToken: String?,
        var videoList: List<Video.Search>,
        private val totalResults: Int
) : Serializable {

    val hasNextPage: Boolean
        get() = totalResults > videoList.size

}