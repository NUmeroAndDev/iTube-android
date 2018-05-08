package com.numero.itube.di

import com.numero.itube.activity.PlayerActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityModule {

    @ContributesAndroidInjector
    abstract fun contributePlayerActivity(): PlayerActivity
}