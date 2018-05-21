package com.numero.itube.repository.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * 保存用のModel
 */
@Entity(tableName = "FavoriteVideo")
data class FavoriteVideo(
        @PrimaryKey
        val id: String,
        val publishedAt: String,
        val title: String,
        val channelId: String,
        val channelTitle: String,
        val thumbnailUrl: String
) : Serializable
