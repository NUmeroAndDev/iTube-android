package com.numero.itube.di

import android.content.Context
import androidx.room.Room
import com.numero.itube.repository.db.FavoriteVideoDao
import com.numero.itube.repository.db.FavoriteVideoDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DBModule {

    @Provides
    @Singleton
    fun provideFavoriteVideoDB(context: Context): FavoriteVideoDatabase {
        return Room.databaseBuilder(context, FavoriteVideoDatabase::class.java, "FavoriteDB.db").build()
    }

    @Provides
    @Singleton
    fun provideFavoriteVideoDao(database: FavoriteVideoDatabase): FavoriteVideoDao {
        return database.favoriteVideoDao()
    }
}