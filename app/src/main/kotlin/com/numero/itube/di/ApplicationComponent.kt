package com.numero.itube.di

import com.numero.itube.activity.PlayerActivity
import com.numero.itube.activity.SearchActivity
import com.numero.itube.fragment.ChannelVideoListFragment
import com.numero.itube.fragment.FavoriteVideoListFragment
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
    fun inject(channelVideoListFragment: ChannelVideoListFragment)

    fun inject(searchActivity: SearchActivity)
    fun inject(playerActivity: PlayerActivity)
}
