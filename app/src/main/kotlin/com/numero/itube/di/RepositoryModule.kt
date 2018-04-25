package com.numero.itube.di

import com.numero.itube.api.YoutubeApi
import com.numero.itube.repository.FavoriteVideoRepository
import com.numero.itube.repository.YoutubeRepository
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
    fun provideFavoriteVideoRepository(favoriteVideoDao: FavoriteVideoDao): FavoriteVideoRepository {
        return FavoriteVideoRepository(favoriteVideoDao)
    }
}