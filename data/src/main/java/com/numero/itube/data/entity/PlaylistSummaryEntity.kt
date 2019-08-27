package com.numero.itube.data.entity

data class PlaylistSummaryEntity(
        val playlistId: Long,
        val playlistTitle: String,
        val videoCount: Int,
        val firstVideoThumbnail: String?
)