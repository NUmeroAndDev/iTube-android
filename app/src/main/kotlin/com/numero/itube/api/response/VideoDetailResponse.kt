package com.numero.itube.api.response

import com.numero.itube.model.VideoDetail
import se.ansman.kotshi.JsonSerializable
import java.io.Serializable

@JsonSerializable
data class VideoDetailResponse(
        val kind: String,
        val etag: String,
        val pageInfo: PageInfo,
        val items: List<VideoDetail>
) : Serializable {

    @JsonSerializable
    data class PageInfo(
            val totalResults: Int,
            val resultsPerPage: Int
    ) : Serializable
}