package com.numero.itube.di

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import com.numero.itube.viewmodel.SearchVideoViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Module
class ApplicationModule(private val application: Application) {

    @Provides
    @Singleton
    fun provideApplicationContext(): Context {
        return application.applicationContext
    }
}