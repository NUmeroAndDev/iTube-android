package com.numero.itube.viewmodel

import androidx.lifecycle.*
import com.numero.itube.api.request.ChannelVideoRequest
import com.numero.itube.api.response.Result
import com.numero.itube.extension.zipLiveData
import com.numero.itube.model.Action
import com.numero.itube.model.ChannelDetail
import com.numero.itube.model.ChannelVideoList
import com.numero.itube.repository.ChannelRepository
import com.numero.itube.repository.ConfigRepository
import javax.inject.Inject

class ChannelVideoListViewModel @Inject constructor(
        private val configRepository: ConfigRepository,// FIXME
        private val channelRepository: ChannelRepository
) : ViewModel(), IErrorViewModel, IProgressViewModel {

    private val actionLiveData: MutableLiveData<Action<ChannelVideoRequest>> = MutableLiveData()
    private val stateLiveData: LiveData<Pair<Result<ChannelDetail>, Result<ChannelVideoList>>> = actionLiveData.switchMap {
        zipLiveData(
                channelRepository.fetchChannelDetail(it.value.key, it.value.channelId),
                channelRepository.fetchChannelVideoList(it.value)
        )
    }
    private val _channelVideoListLiveData: MediatorLiveData<ChannelVideoList> = MediatorLiveData()
    private val _channelDetailLiveData: MediatorLiveData<ChannelDetail> = MediatorLiveData()

    val channelVideoListLiveData: LiveData<ChannelVideoList> = _channelVideoListLiveData
    val channelDetail: LiveData<ChannelDetail> = _channelDetailLiveData

    override val error: MutableLiveData<Throwable> = MutableLiveData()
    override val isShowError: MutableLiveData<Boolean> = MutableLiveData()
    override val isShowProgress: MutableLiveData<Boolean> = MutableLiveData()

    init {
        _channelVideoListLiveData.addSource(stateLiveData) {
            val result = it.second
            if (result is Result.Success) {
                _channelVideoListLiveData.postValue(result.response)
            }
        }
        _channelDetailLiveData.addSource(stateLiveData) {
            val result = it.first
            if (result is Result.Success) {
                _channelDetailLiveData.postValue(result.response)
            }
        }
    }

    fun executeLoadChannelVideo(channelId: String) {
        val request = ChannelVideoRequest(configRepository.apiKey, channelId)
        actionLiveData.value = Action(request)
    }

    fun executeMoreLoad() {
        val nextPageToken = channelVideoListLiveData.value?.nextPageToken ?: return
        val action = actionLiveData.value ?: return
        val request = ChannelVideoRequest(
                configRepository.apiKey,
                action.value.channelId,
                nextPageToken)
        actionLiveData.value = Action(request)
    }
}