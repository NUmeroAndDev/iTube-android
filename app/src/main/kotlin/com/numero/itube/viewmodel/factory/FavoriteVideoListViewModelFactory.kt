package com.numero.itube.viewmodel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.numero.itube.repository.IFavoriteVideoRepository
import com.numero.itube.viewmodel.FavoriteVideoListViewModel

class FavoriteVideoListViewModelFactory(private val favoriteRepository: IFavoriteVideoRepository) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return FavoriteVideoListViewModel(favoriteRepository) as T
    }
}