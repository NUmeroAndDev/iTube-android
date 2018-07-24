package com.numero.itube.presenter

import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.contract.SearchVideoContract
import com.numero.itube.repository.IYoutubeRepository
import com.numero.itube.viewmodel.SearchVideoViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy

class SearchVideoPresenter(
        private val viewModel: SearchVideoViewModel,
        private val youtubeRepository: IYoutubeRepository
) : SearchVideoContract.Presenter {

    override fun subscribe() {
    }

    override fun unSubscribe() {
    }

    override fun search(key: String, searchWord: String) {
        viewModel.searchWord = searchWord
        val request = SearchVideoRequest(key, searchWord)
        executeSearch(request)
    }

    override fun requestMore(key: String) {
        val searchWord = viewModel.searchWord ?: return
        val request = SearchVideoRequest(key, searchWord, viewModel.nextPageToken.value)
        executeSearch(request)
    }

    override fun retry(key: String) {
        val searchWord = viewModel.searchWord ?: return
        val request = SearchVideoRequest(key, searchWord, viewModel.nextPageToken.value)
        executeSearch(request)
    }

    override fun clear() {
        viewModel.searchWord = null
        viewModel.videoList.postValue(mutableListOf())
        viewModel.isShowError.postValue(false)
    }

    private fun executeSearch(request: SearchVideoRequest) {
        viewModel.progress.postValue(true)
        youtubeRepository.loadSearchResponse(request)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            viewModel.progress.postValue(false)

                            viewModel.nextPageToken.postValue(it.nextPageToken)
                            viewModel.videoList.postValue(it.videoList)
                            viewModel.hasNextPage.postValue(it.hasNextPage)
                        },
                        onError = {
                            viewModel.progress.postValue(false)
                            viewModel.isShowError.postValue(true)

                            viewModel.error.postValue(it)
                        }
                )
    }
}