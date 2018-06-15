package com.numero.itube.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.repository.IYoutubeRepository
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class SearchVideoViewModel(private val youtubeRepository: IYoutubeRepository) : ViewModel(), IErrorViewModel, IProgressViewModel {

    private val job = Job()

    val videoList: MutableLiveData<List<SearchResponse.Video>> = MutableLiveData()
    val nextPageToken: MutableLiveData<String> = MutableLiveData()

    override val error: MutableLiveData<Throwable> = MutableLiveData()
    override val isShowError: MutableLiveData<Boolean> = MutableLiveData()
    override val progress: MutableLiveData<Boolean> = MutableLiveData()

    fun search(key: String, searchWord: String, nestPageToken: String? = null) {
        executeSearch(key, searchWord, nestPageToken)
    }

    private fun executeSearch(key: String, searchWord: String, nestPageToken: String?) = async(job + UI) {
        isShowError.postValue(false)
        progress.postValue(true)
        try {
            val request = SearchVideoRequest(key, searchWord, nestPageToken)
            val response = youtubeRepository.search(request).await()
            val old = videoList.value
            if (old == null) {
                videoList.postValue(response.items)
            } else {
                // 既存のリストを追加してリスト全体を返す
                val list = mutableListOf<SearchResponse.Video>().apply {
                    addAll(old)
                    addAll(response.items)
                }
                videoList.postValue(list)
            }
            nextPageToken.postValue(response.nextPageToken)
        } catch (t: Throwable) {
            if (nestPageToken == null) {
                isShowError.postValue(true)
                error.postValue(t)
            }
            //TODO ページングでエラー出た時の処理
        } finally {
            progress.postValue(false)
        }
    }
}