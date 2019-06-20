package com.numero.itube.api.response

import com.numero.itube.model.Video
import java.io.Serializable

data class VideoResponse(
        val nextPageToken: String?,
        var videoList: List<Video.Search>,
        private val totalResults: Int
) : Serializable {

    val hasNextPage: Boolean
        get() = totalResults > videoList.size

}