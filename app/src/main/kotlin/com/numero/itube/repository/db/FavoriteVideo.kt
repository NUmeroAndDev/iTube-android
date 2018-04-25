package com.numero.itube.repository.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import java.io.Serializable


/**
 * 保存用のModel
 */
@Entity
data class FavoriteVideo(
        @PrimaryKey
        val id: String,
        val publishedAt: String,
        val title: String,
        val channelId: String,
        val channelTitle: String,
        val thumbnailUrl: String
) : Serializable
