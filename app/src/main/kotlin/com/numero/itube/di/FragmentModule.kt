package com.numero.itube.di

import com.numero.itube.fragment.DetailFragment
import com.numero.itube.fragment.SearchFragment
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class FragmentModule {

    @ContributesAndroidInjector
    abstract fun contributeSearchFragment(): SearchFragment

    @ContributesAndroidInjector
    abstract fun contributeDetailFragment(): DetailFragment
}