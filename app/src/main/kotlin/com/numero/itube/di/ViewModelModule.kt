package com.numero.itube.di

import androidx.lifecycle.ViewModel
import com.numero.itube.ui.playlist.PlaylistListViewModel
import com.numero.itube.ui.top.TopViewModel
import com.numero.itube.ui.video.SelectPlaylistViewModel
import com.numero.itube.ui.video.detail.playlist.DetailInPlaylistViewModel
import com.numero.itube.ui.video.detail.VideoDetailViewModel
import com.numero.itube.ui.video.detail.search.DetailInSearchViewModel
import com.numero.itube.ui.channel.ChannelVideoListViewModel
import com.numero.itube.ui.search.SearchVideoViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(PlaylistListViewModel::class)
    abstract fun bindPlaylistListViewModel(viewModel: PlaylistListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(VideoDetailViewModel::class)
    abstract fun bindVideoDetailViewModel(viewModel: VideoDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailInPlaylistViewModel::class)
    abstract fun bindDetailInPlaylistViewModel(viewModel: DetailInPlaylistViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(DetailInSearchViewModel::class)
    abstract fun bindDetailInSearchViewModel(viewModel: DetailInSearchViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TopViewModel::class)
    abstract fun bindTopViewModel(viewModel: TopViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SelectPlaylistViewModel::class)
    abstract fun bindSelectPlaylistViewModel(viewModel: SelectPlaylistViewModel): ViewModel

}