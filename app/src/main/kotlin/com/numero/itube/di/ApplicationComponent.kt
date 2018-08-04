package com.numero.itube.di

import com.numero.itube.activity.*
import com.numero.itube.fragment.RoundedBottomSheetDialogFragment
import com.numero.itube.fragment.SettingsFragment
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
    fun inject(topActivity: TopActivity)
    fun inject(searchActivity: SearchActivity)
    fun inject(playerActivity: PlayerActivity)
    fun inject(channelDetailActivity: ChannelDetailActivity)
    fun inject(settingsActivity: SettingsActivity)

    fun inject(roundedBottomSheetDialogFragment: RoundedBottomSheetDialogFragment)
    fun inject(settingsFragment: SettingsFragment)
}
