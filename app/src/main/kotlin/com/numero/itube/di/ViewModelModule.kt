package com.numero.itube.di

import androidx.lifecycle.ViewModel
import com.numero.itube.ui.playlist.PlaylistListViewModel
import com.numero.itube.ui.video.SelectPlaylistViewModel
import com.numero.itube.ui.video.VideoDetailViewModel
import com.numero.itube.viewmodel.ChannelVideoListViewModel
import com.numero.itube.viewmodel.PlayerViewModel
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

    @Binds
    @IntoMap
    @ViewModelKey(PlayerViewModel::class)
    abstract fun bindPlayerViewModel(viewModel: PlayerViewModel): ViewModel

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
    @ViewModelKey(SelectPlaylistViewModel::class)
    abstract fun bindSelectPlaylistViewModel(viewModel: SelectPlaylistViewModel): ViewModel

}