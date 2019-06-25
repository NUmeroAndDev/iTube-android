package com.numero.itube.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.numero.itube.data.entity.PlaylistEntity
import com.numero.itube.data.entity.VideoEntity
import com.numero.itube.data.entity.VideoLinkingPlaylistEntity

@Database(entities = [
    VideoEntity::class,
    PlaylistEntity::class,
    VideoLinkingPlaylistEntity::class
], version = 1)
abstract class PlaylistDatabase : RoomDatabase() {

    abstract fun playlistDao(): PlaylistDao

}