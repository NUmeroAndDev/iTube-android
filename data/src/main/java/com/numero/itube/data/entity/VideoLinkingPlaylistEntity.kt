package com.numero.itube.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "VideoLinkingPlaylist")
data class VideoLinkingPlaylistEntity(
        @PrimaryKey
        val videoId: String,
        var playlistId: Long
)