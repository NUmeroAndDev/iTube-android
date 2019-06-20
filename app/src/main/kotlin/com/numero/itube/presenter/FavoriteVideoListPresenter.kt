package com.numero.itube.presenter

import com.numero.itube.repository.IFavoriteVideoRepository
import com.numero.itube.viewmodel.FavoriteVideoListViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteVideoListPresenter(
        private val viewModel: FavoriteVideoListViewModel,
        private val favoriteRepository: IFavoriteVideoRepository
) : IFavoriteVideoListPresenter {

    override fun subscribe() {
    }

    override fun unSubscribe() {
    }

    override fun loadFavoriteVideoList() {
        viewModel.isShowProgress.postValue(true)
        GlobalScope.launch(Dispatchers.Main) {
            val list = async(Dispatchers.Default) { favoriteRepository.loadFavoriteVideo() }.await()
            viewModel.isShowProgress.postValue(false)
            viewModel.videoList.postValue(list)
            viewModel.isShowEmptyMessage.postValue(list.value.isEmpty())
        }
    }
}