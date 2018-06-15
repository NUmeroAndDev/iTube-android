package com.numero.itube.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.numero.itube.repository.IYoutubeRepository
import com.numero.itube.viewmodel.SearchVideoViewModel

class SearchVideoViewModelFactory(private val youtubeRepository: IYoutubeRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchVideoViewModel(youtubeRepository) as T
    }
}