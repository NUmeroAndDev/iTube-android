package com.numero.itube.di

import android.content.Context
import com.numero.itube.api.YoutubeApi
import com.numero.itube.data.PlaylistDataSource
import com.numero.itube.data.PlaylistDataSourceImpl
import com.numero.itube.data.YoutubeDataSource
import com.numero.itube.data.YoutubeDataSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataSourceModule(
        private val apiKey: String
) {

    @Provides
    @Singleton
    fun provideVideoDataSource(youtubeApi: YoutubeApi): YoutubeDataSource {
        return YoutubeDataSourceImpl(youtubeApi, apiKey)
    }

    @Provides
    @Singleton
    fun providePlaylistDataSource(context: Context): PlaylistDataSource {
        return PlaylistDataSourceImpl(context)
    }
}