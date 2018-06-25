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

    val videoList: LiveData<List<SearchResponse.Video>> = Transformations.map(responseLiveData) {
        when (it) {
            is Response.Success -> it.response.videoList
            is Response.Error -> null
        }
    }
    val nextPageToken: LiveData<String> = Transformations.map(responseLiveData) {
        when (it) {
            is Response.Success -> it.response.nextPageToken
            else -> null
        }
    }

    override val error: LiveData<Throwable> = Transformations.map(responseLiveData) {
        when(it) {
            is Response.Error -> it.throwable
            else -> null
        }
    }
    override val isShowError: LiveData<Boolean> = Transformations.map(responseLiveData) {
        it is Response.Error
    }
    override val progress: LiveData<Boolean> = youtubeRepository.isProgressLiveData

    fun search(key: String, searchWord: String, nestPageToken: String? = null) {
        val request = SearchVideoRequest(key, searchWord, nestPageToken)
        requestLiveData.postValue(request)
    }
}