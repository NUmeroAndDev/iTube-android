package com.numero.itube.di

import android.content.Context
import com.numero.itube.api.YoutubeApi
import com.numero.itube.repository.*
import com.numero.itube.repository.db.FavoriteVideoDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RepositoryModule {

    @Provides
    @Singleton
    fun provideYoutubeRepository(youtubeApi: YoutubeApi): YoutubeRepository {
        return YoutubeRepository(youtubeApi)
    }

    @Provides
    @Singleton
    fun provideVideoRepository(youtubeApi: YoutubeApi): VideoRepository {
        return VideoRepositoryImpl(youtubeApi)
    }

    @Provides
    @Singleton
    fun provideChannelRepository(youtubeApi: YoutubeApi): ChannelRepository {
        return ChannelRepositoryImpl(youtubeApi)
    }

    @Provides
    @Singleton
    fun provideFavoriteVideoRepository(favoriteVideoDao: FavoriteVideoDao): FavoriteVideoRepository {
        return FavoriteVideoRepository(favoriteVideoDao)
    }

    @Provides
    @Singleton
    fun provideConfigRepository(context: Context): ConfigRepository {
        return ConfigRepository(context)
    }
}