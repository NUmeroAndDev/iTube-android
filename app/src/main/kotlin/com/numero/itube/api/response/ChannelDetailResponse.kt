package com.numero.itube.api.response

import com.numero.itube.model.ChannelDetail
import se.ansman.kotshi.JsonSerializable
import java.io.Serializable

@JsonSerializable
data class ChannelDetailResponse(
        val kind: String,
        val etag: String,
        val items: List<ChannelDetail>
) : Serializable