package com.numero.itube.di

import com.numero.itube.activity.PlayerActivity
import com.numero.itube.activity.SearchActivity
import com.numero.itube.fragment.*
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    (ApplicationModule::class),
    (ApiClientModule::class),
    (DBModule::class),
    (RepositoryModule::class)
])
interface ApplicationComponent {
    fun inject(favoriteVideoListFragment: FavoriteVideoListFragment)
    fun inject(relativeFavoriteFragment: RelativeFavoriteFragment)
    fun inject(relativeFragment: RelativeFragment)
    fun inject(searchFragment: SearchFragment)
    fun inject(channelVideoListFragment: ChannelVideoListFragment)

    fun inject(searchActivity: SearchActivity)
    fun inject(playerActivity: PlayerActivity)
}
