package com.numero.itube.di

import androidx.lifecycle.ViewModel
import com.numero.itube.viewmodel.ChannelVideoListViewModel
import com.numero.itube.viewmodel.SearchVideoViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {

    @Binds
    @IntoMap
    @ViewModelKey(SearchVideoViewModel::class)
    abstract fun bindSearchVideoViewModel(viewModel: SearchVideoViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChannelVideoListViewModel::class)
    abstract fun bindChannelVideoListViewModel(viewModel: ChannelVideoListViewModel): ViewModel
}