package com.numero.itube.api.response

import com.numero.itube.model.Channel
import se.ansman.kotshi.JsonSerializable
import java.io.Serializable

@JsonSerializable
data class ChannelResponse(
        val kind: String,
        val etag: String,
        val nextPageToken: String?,
        val pageInfo: PageInfo,
        val items: List<Channel>
) : Serializable {

    @JsonSerializable
    data class PageInfo(
            val totalResults: Int,
            val resultsPerPage: Int
    ) : Serializable
}