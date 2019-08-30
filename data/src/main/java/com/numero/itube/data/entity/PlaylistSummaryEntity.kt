package com.numero.itube.data.entity

import androidx.room.Embedded
import androidx.room.Entity

@Entity
data class PlaylistSummaryEntity(
        val playlistId: Long,
        val playlistTitle: String,
        val videoCount: Int,
        @Embedded val videoEntity: VideoEntity?
)