package com.numero.itube.di

import android.content.Context
import com.numero.itube.R
import com.numero.itube.api.YoutubeApi
import com.numero.itube.data.VideoDataSource
import com.numero.itube.data.VideoDataSourceImpl
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DataSourceModule(
        private val apiKey: String
) {

    @Provides
    @Singleton
    fun provideVideoDataSource(youtubeApi: YoutubeApi): VideoDataSource {
        return VideoDataSourceImpl(youtubeApi, apiKey)
    }
}