package com.numero.itube.viewmodel

import androidx.lifecycle.*
import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.api.response.Result
import com.numero.itube.model.Action
import com.numero.itube.model.SearchVideoList
import com.numero.itube.repository.VideoRepository
import javax.inject.Inject

class SearchVideoViewModel @Inject constructor(
        private val videoRepository: VideoRepository
) : ViewModel(), IErrorViewModel, IProgressViewModel {

    private val actionLiveData: MutableLiveData<Action<SearchVideoRequest>> = MutableLiveData()
    private val stateLiveData: LiveData<Result<SearchVideoList>> = actionLiveData.switchMap {
        videoRepository.fetchVideoList(it.value)
    }
    private val _searchVideoListLiveData: MediatorLiveData<SearchVideoList> = MediatorLiveData()

    val searchVideoListLiveData: LiveData<SearchVideoList> = _searchVideoListLiveData

    override val error: MutableLiveData<Throwable> = MutableLiveData()
    override val isShowError: MutableLiveData<Boolean> = MutableLiveData()
    override val isShowProgress: MutableLiveData<Boolean> = MutableLiveData()

    init {
        _searchVideoListLiveData.addSource(stateLiveData) {
            if (it is Result.Success) {
                _searchVideoListLiveData.postValue(it.response)
            }
        }
    }

    fun executeSearch(searchWord: String) {
        val request = SearchVideoRequest(searchWord)
        actionLiveData.value = Action(request)
    }

    fun executeMoreLoad() {
        val nextPageToken = searchVideoListLiveData.value?.nextPageToken ?: return
        val action = actionLiveData.value ?: return
        val request = SearchVideoRequest(action.value.searchWord, nextPageToken)
        actionLiveData.value = Action(request)
    }

    fun retry() {
        // TODO
    }
}