package com.numero.itube.presenter

import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.repository.IConfigRepository
import com.numero.itube.repository.IYoutubeRepository
import com.numero.itube.viewmodel.SearchVideoViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

class SearchVideoPresenter(
        private val viewModel: SearchVideoViewModel,
        private val youtubeRepository: IYoutubeRepository,
        private val configRepository: IConfigRepository
) : ISearchVideoPresenter {

    override fun subscribe() {
    }

    override fun unSubscribe() {
    }

    override fun search(searchWord: String) {
        viewModel.searchWord = searchWord
        val request = SearchVideoRequest(configRepository.apiKey, searchWord)
        executeSearch(request)
    }

    override fun requestMore() {
        val searchWord = viewModel.searchWord ?: return
        val request = SearchVideoRequest(configRepository.apiKey, searchWord, viewModel.nextPageToken.value)
        executeSearch(request)
    }

    override fun retry() {
        val searchWord = viewModel.searchWord ?: return
        val request = SearchVideoRequest(configRepository.apiKey, searchWord, viewModel.nextPageToken.value)
        executeSearch(request)
    }

    override fun clear() {
        viewModel.searchWord = null
        viewModel.videoList.postValue(mutableListOf())
        viewModel.isShowError.postValue(false)
    }

    private fun executeSearch(request: SearchVideoRequest) {
        if (viewModel.isProcessing) {
            return
        }

        viewModel.isShowProgress.postValue(true)
        viewModel.isShowError.postValue(false)

        youtubeRepository.loadSearchResponse(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            viewModel.isShowProgress.postValue(false)

                            viewModel.nextPageToken.postValue(it.nextPageToken)
                            viewModel.videoList.postValue(it.videoList)
                            viewModel.hasNextPage.postValue(it.hasNextPage)
                        },
                        onError = {
                            viewModel.isShowProgress.postValue(false)
                            viewModel.isShowError.postValue(true)

                            viewModel.error.postValue(it)
                        }
                )
    }
}