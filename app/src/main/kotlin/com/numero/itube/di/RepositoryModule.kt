package com.numero.itube.di

import android.content.Context
import com.numero.itube.data.PlaylistDataSource
import com.numero.itube.data.YoutubeDataSource
import com.numero.itube.repository.*
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideVideoRepository(youtubeDataSource: YoutubeDataSource): VideoRepository {
        return VideoRepositoryImpl(youtubeDataSource)
    }

    @Provides
    @Singleton
    fun provideChannelRepository(youtubeDataSource: YoutubeDataSource): ChannelRepository {
        return ChannelRepositoryImpl(youtubeDataSource)
    }

    @Provides
    @Singleton
    fun providePlaylistRepository(playlistDataSource: PlaylistDataSource): PlaylistRepository {
        return PlaylistRepositoryImpl(playlistDataSource)
    }

    @Provides
    @Singleton
    fun provideConfigRepository(context: Context): ConfigRepository {
        return ConfigRepository(context)
    }
}