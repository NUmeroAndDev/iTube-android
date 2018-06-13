package com.numero.itube.presenter

import com.numero.itube.contract.SearchContract
import com.numero.itube.repository.IYoutubeRepository
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.subscribeBy

class SearchPresenter(
        private val view: SearchContract.View,
        private val youtubeRepository: IYoutubeRepository) : SearchContract.Presenter {

    private var disposable: Disposable? = null

    init {
        view.setPresenter(this)
    }

    override fun subscribe() {
    }

    override fun unSubscribe() {
        disposable?.apply {
            if (isDisposed.not()) {
                dispose()
            }
        }
    }

    override fun search(key: String, searchWord: String, nestPageToken: String?) {
        executeSearch(key, searchWord, nestPageToken)
    }

    private fun executeSearch(key: String, searchWord: String, nestPageToken: String?) {
        view.hideErrorMessage()
        view.showProgress()
        disposable = youtubeRepository.search(key, searchWord, nestPageToken)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy(
                        onNext = {
                            if (nestPageToken == null) {
                                view.showVideoList(it.items, it.nextPageToken)
                            } else {
                                view.addVideoList(it.items, it.nextPageToken)
                            }
                            view.dismissProgress()
                        },
                        onError = {
                            if (nestPageToken == null) {
                                view.showErrorMessage(it)
                            }
                            //TODO ページングでエラー出た時の処理
                            view.dismissProgress()
                        }
                )
    }
}