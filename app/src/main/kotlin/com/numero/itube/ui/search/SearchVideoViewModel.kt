package com.numero.itube.ui.search

import androidx.lifecycle.*
import com.numero.itube.api.request.SearchVideoRequest
import com.numero.itube.api.response.Result
import com.numero.itube.model.Action
import com.numero.itube.model.SearchVideoList
import com.numero.itube.model.State
import com.numero.itube.repository.VideoRepository
import javax.inject.Inject

class SearchVideoViewModel @Inject constructor(
        private val videoRepository: VideoRepository
) : ViewModel() {

    private val actionLiveData: MutableLiveData<Action<SearchVideoRequest>> = MutableLiveData()
    private val stateLiveData: LiveData<State<SearchVideoList>> = actionLiveData.switchMap {
        videoRepository.fetchVideoList(it.value)
    }
    private val _searchVideoListLiveData: MediatorLiveData<SearchVideoList> = MediatorLiveData()

    val searchVideoListLiveData: LiveData<SearchVideoList> = _searchVideoListLiveData

    val errorLiveData: LiveData<Boolean> = stateLiveData.map {
        it is State.Error
    }
    val progressLiveData: LiveData<Boolean> = stateLiveData.map {
        it is State.Progress
    }

    init {
        _searchVideoListLiveData.addSource(stateLiveData) {
            if (it is State.Success) {
                _searchVideoListLiveData.postValue(it.value)
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