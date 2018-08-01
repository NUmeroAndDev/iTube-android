package com.numero.itube.presenter

import com.numero.itube.repository.IFavoriteVideoRepository
import com.numero.itube.viewmodel.FavoriteVideoListViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

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
        favoriteRepository.loadFavoriteVideo()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onSuccess = {
                            viewModel.isShowEmptyMessage.postValue(it.isEmpty())
                            viewModel.isShowProgress.postValue(false)
                            viewModel.videoList.postValue(it)
                        },
                        onError = {
                            viewModel.isShowEmptyMessage.postValue(false)
                            viewModel.isShowProgress.postValue(false)
                            viewModel.error.postValue(it)
                        }
                )
    }
}