package com.numero.itube.di

import com.numero.itube.BuildConfig
import com.numero.itube.api.ApplicationJsonAdapterFactory
import com.numero.itube.api.YoutubeApi
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiClientModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            connectTimeout(TIMEOUT_TIME_SECONDS, TimeUnit.SECONDS)
            readTimeout(TIMEOUT_TIME_SECONDS, TimeUnit.SECONDS)
            writeTimeout(TIMEOUT_TIME_SECONDS, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) {
                addInterceptor(HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                })
            }
        }.build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl("https://www.googleapis.com/youtube/v3/")
                .client(okHttpClient)
                .addConverterFactory(MoshiConverterFactory.create(Moshi.Builder().add(ApplicationJsonAdapterFactory.INSTANCE).build()))
                .build()
    }

    @Provides
    @Singleton
    fun provideYoutubeApi(retrofit: Retrofit): YoutubeApi = retrofit.create(YoutubeApi::class.java)

    companion object {
        private const val TIMEOUT_TIME_SECONDS: Long = 10
    }
}