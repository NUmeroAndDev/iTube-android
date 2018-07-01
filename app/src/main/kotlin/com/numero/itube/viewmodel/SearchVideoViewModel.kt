package com.numero.itube.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.api.response.Response
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.api.response.VideoResponse
import com.numero.itube.repository.IYoutubeRepository

class SearchVideoViewModel(private val youtubeRepository: IYoutubeRepository) : ViewModel(), IErrorViewModel, IProgressViewModel {

    private val requestLiveData = MutableLiveData<SearchVideoRequest>()
    private val responseLiveData: LiveData<Response<VideoResponse>> = Transformations.switchMap(requestLiveData) {
        youtubeRepository.loadSearchResponse(it)
    }

    var searchWord: String? = null
    var nextPageToken: String? = null

    val videoList: LiveData<List<SearchResponse.Video>> = Transformations.map(responseLiveData) {
        when (it) {
            is Response.Success -> {
                nextPageToken = it.response.nextPageToken
                it.response.videoList
            }
            is Response.Error -> null
        }
    }

    override val error: LiveData<Throwable> = Transformations.map(responseLiveData) {
        when (it) {
            is Response.Error -> it.throwable
            else -> null
        }
    }
    override val isShowError: LiveData<Boolean> = Transformations.map(responseLiveData) {
        it is Response.Error
    }
    override val progress: LiveData<Boolean> = youtubeRepository.isProgressLiveData

    fun search(key: String, searchWord: String) {
        this.searchWord = searchWord
        val request = SearchVideoRequest(key, searchWord)
        requestLiveData.postValue(request)
    }

    fun requestMore(key: String) {
        val searchWord = searchWord ?: return
        val nextPageToken = nextPageToken ?: return
        val request = SearchVideoRequest(key, searchWord, nextPageToken)
        requestLiveData.postValue(request)
    }

    fun retry(key: String) {
        val searchWord = searchWord ?: return
        val nextPageToken = nextPageToken
        if (nextPageToken == null) {
            search(key, searchWord)
        } else {
            requestMore(key)
        }
    }
}