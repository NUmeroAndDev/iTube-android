package com.numero.itube.api.request

data class SearchVideoRequest(
        val searchWord: String,
        val nextPageToken: String? = null
) {

    val hasNextPageToken: Boolean
        get() = nextPageToken.isNullOrEmpty().not()

}