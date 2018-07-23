package com.numero.itube.api.response

import java.io.Serializable

data class VideoResponse(
        val nextPageToken: String?,
        var videoList: List<SearchResponse.Video>,
        private val totalResults: Int
) : Serializable {

    val hasNextPage: Boolean
        get() = totalResults > videoList.size

}