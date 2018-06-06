package com.numero.itube.di

import com.numero.itube.activity.PlayerActivity
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
    fun inject(detailFragment: DetailFragment)
    fun inject(favoriteFragment: FavoriteFragment)
    fun inject(relativeFavoriteFragment: RelativeFavoriteFragment)
    fun inject(relativeFragment: RelativeFragment)
    fun inject(searchFragment: SearchFragment)
    fun inject(channelVideoFragment: ChannelVideoFragment)

    fun inject(playerActivity: PlayerActivity)
}
