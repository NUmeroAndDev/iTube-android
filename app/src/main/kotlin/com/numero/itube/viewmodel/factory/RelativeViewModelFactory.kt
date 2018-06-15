package com.numero.itube.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.numero.itube.repository.IFavoriteVideoRepository
import com.numero.itube.repository.IYoutubeRepository
import com.numero.itube.viewmodel.RelativeViewModel

class RelativeViewModelFactory(
        private val youtubeRepository: IYoutubeRepository,
        private val favoriteRepository: IFavoriteVideoRepository,
        private val videoId: String,
        private val channelId: String
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return RelativeViewModel(youtubeRepository, favoriteRepository, videoId, channelId) as T
    }
}