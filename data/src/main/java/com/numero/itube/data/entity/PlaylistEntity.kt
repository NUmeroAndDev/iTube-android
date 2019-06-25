package com.numero.itube.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Playlist")
data class PlaylistEntity(
        @PrimaryKey(autoGenerate = true)
        val id: Long,
        val title: String
)
