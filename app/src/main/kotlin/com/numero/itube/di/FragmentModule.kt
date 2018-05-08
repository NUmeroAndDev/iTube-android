package com.numero.itube.di

import com.numero.itube.fragment.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeFavoriteFragment(): FavoriteFragment

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailFragment(): DetailFragment

    @ContributesAndroidInjector
    abstract fun contributeRelativeFragment(): RelativeFragment

    @ContributesAndroidInjector
    abstract fun contributeRelativeFavoriteFragment(): RelativeFavoriteFragment

    @ContributesAndroidInjector
    abstract fun contributePlayerSettingsBottomSheetFragment(): PlayerSettingsBottomSheetFragment
}