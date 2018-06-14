package com.numero.itube.api.request

data class SearchVideoRequest(
        override val key: String,
        val searchWord: String,
        val nextPageToken: String? = null
) : YoutubeApiRequest {

    val hasNextPageToken: Boolean
        get() = nextPageToken.isNullOrEmpty().not()

}