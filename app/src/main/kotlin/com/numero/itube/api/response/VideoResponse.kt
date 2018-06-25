package com.numero.itube.api.response

import java.io.Serializable

data class VideoResponse(
        val nextPageToken: String?,
        var videoList: List<SearchResponse.Video>
) : Serializable