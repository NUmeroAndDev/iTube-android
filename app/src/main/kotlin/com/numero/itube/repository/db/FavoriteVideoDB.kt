package com.numero.itube.repository.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.RoomDatabase

@Database(entities = [(FavoriteVideoDao::class)], version = 1)
abstract class FavoriteVideoDB : RoomDatabase() {

    abstract fun favoriteVideoDao(): FavoriteVideoDao

}