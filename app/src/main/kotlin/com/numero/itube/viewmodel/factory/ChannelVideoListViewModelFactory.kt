package com.numero.itube.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.numero.itube.repository.IYoutubeRepository
import com.numero.itube.viewmodel.ChannelVideoListViewModel

class ChannelVideoListViewModelFactory(
        private val youtubeRepository: IYoutubeRepository,
        private val channelId: String
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChannelVideoListViewModel(youtubeRepository, channelId) as T
    }
}