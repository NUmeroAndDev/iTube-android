package com.numero.itube.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.api.response.Response
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.repository.IYoutubeRepository

class SearchVideoViewModel(private val youtubeRepository: IYoutubeRepository) : ViewModel(), IErrorViewModel, IProgressViewModel {

    private val requestLiveData = MutableLiveData<SearchVideoRequest>()
    private val responseLiveData: LiveData<Response<SearchResponse>> = Transformations.switchMap(requestLiveData) {
        youtubeRepository.loadSearchResponse(it)
    }

    val videoList: LiveData<List<SearchResponse.Video>> = Transformations.map(responseLiveData) {
        // FIXME ページング
        progress.postValue(false)
        when (it) {
            is Response.Success -> {
                isShowError.postValue(false)
                it.response.items
            }
            else -> {
                isShowError.postValue(true)
                error.postValue(it.throwable)
                null
            }
        }
    }
    val nextPageToken: LiveData<String> = Transformations.map(responseLiveData) {
        when (it) {
            is Response.Success -> it.response.nextPageToken
            else -> null
        }
    }

    override val error: MutableLiveData<Throwable> = MutableLiveData()
    override val isShowError: MutableLiveData<Boolean> = MutableLiveData()
    override val progress: MutableLiveData<Boolean> = MutableLiveData()

    fun search(key: String, searchWord: String, nestPageToken: String? = null) {
        val request = SearchVideoRequest(key, searchWord, nestPageToken)
        progress.postValue(true)
        isShowError.postValue(false)
        requestLiveData.postValue(request)
    }

//    private fun executeSearch(key: String, searchWord: String, nestPageToken: String?) = async(job + UI) {
//        isShowError.postValue(false)
//        progress.postValue(true)
//        try {
//            val request = SearchVideoRequest(key, searchWord, nestPageToken)
//            val response = youtubeRepository.search(request).await()
//            val old = videoList.value
//            if (old == null) {
//                videoList.postValue(response.items)
//            } else {
//                // 既存のリストを追加してリスト全体を返す
//                val list = mutableListOf<SearchResponse.Video>().apply {
//                    addAll(old)
//                    addAll(response.items)
//                }
//                videoList.postValue(list)
//            }
//            nextPageToken.postValue(response.nextPageToken)
//        } catch (t: Throwable) {
//            if (nestPageToken == null) {
//                isShowError.postValue(true)
//                error.postValue(t)
//            }
//            //TODO ページングでエラー出た時の処理
//        } finally {
//            progress.postValue(false)
//        }
//    }
}