package com.numero.itube.di

import com.numero.itube.ui.channel.ChannelDetailActivity
import com.numero.itube.activity.LicensesActivity
import com.numero.itube.activity.SettingsActivity
import com.numero.itube.fragment.SettingsFragment
import com.numero.itube.ui.search.SearchActivity
import com.numero.itube.ui.top.TopActivity
import com.numero.itube.ui.video.SelectPlaylistBottomSheetFragment
import com.numero.itube.ui.video.detail.VideoDetailActivity
import com.numero.itube.ui.video.detail.playlist.DetailInPlaylistFragment
import com.numero.itube.ui.video.detail.search.DetailInSearchFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ApplicationModule::class,
    ApiClientModule::class,
    DataSourceModule::class,
    RepositoryModule::class,
    ViewModelModule::class,
    ViewModelFactoryModule::class
])
interface ApplicationComponent {
    fun inject(searchActivity: SearchActivity)
    fun inject(videoDetailActivity: VideoDetailActivity)
    fun inject(channelDetailActivity: ChannelDetailActivity)
    fun inject(settingsActivity: SettingsActivity)
    fun inject(licensesActivity: LicensesActivity)
    fun inject(topActivity: TopActivity)

    fun inject(detailInPlaylistFragment: DetailInPlaylistFragment)
    fun inject(detailInSearchFragment: DetailInSearchFragment)

    fun inject(settingsFragment: SettingsFragment)
    fun inject(selectPlaylistBottomSheetFragment: SelectPlaylistBottomSheetFragment)
}
