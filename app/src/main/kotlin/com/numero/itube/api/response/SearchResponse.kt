package com.numero.itube.api.response

import com.numero.itube.model.Video
import se.ansman.kotshi.JsonSerializable
import java.io.Serializable

@JsonSerializable
data class SearchResponse(
        val kind: String,
        val etag: String,
        val nextPageToken: String,
        val regionCode: String,
        val pageInfo: PageInfo,
        val items: List<Video>
) : Serializable {

    @JsonSerializable
    data class PageInfo(
            val totalResults: Int,
            val resultsPerPage: Int
    ) : Serializable
}