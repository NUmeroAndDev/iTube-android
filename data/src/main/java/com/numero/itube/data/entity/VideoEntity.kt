package com.numero.itube.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Video")
data class VideoEntity(
        @PrimaryKey
        val id: String,
        val title: String,
        val channelId: String,
        val channelTitle: String,
        val thumbnailUrl: String
)
