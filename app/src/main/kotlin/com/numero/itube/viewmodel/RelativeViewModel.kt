package com.numero.itube.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.numero.itube.api.request.RelativeRequest
import com.numero.itube.api.response.ChannelResponse
import com.numero.itube.api.response.Response
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.api.response.VideoDetailResponse
import com.numero.itube.repository.IFavoriteVideoRepository
import com.numero.itube.repository.IYoutubeRepository
import com.numero.itube.repository.db.FavoriteVideo
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class RelativeViewModel(
        private val youtubeRepository: IYoutubeRepository,
        private val favoriteRepository: IFavoriteVideoRepository,
        private val videoId: String,
        private val channelId: String
) : ViewModel(), IErrorViewModel, IProgressViewModel {

    private val job = Job()

    private val relativeRequestLiveData = MutableLiveData<RelativeRequest>()
    private val relativeResponseLiveData = Transformations.switchMap(relativeRequestLiveData) {
        youtubeRepository.loadRelative(it)
    }

    val videoList: LiveData<List<SearchResponse.Video>> = Transformations.map(relativeResponseLiveData) {
        when (it) {
            is Response.Success -> it.response.searchResponse.items
            else -> null
        }
    }
    val videoDetail: LiveData<VideoDetailResponse.VideoDetail> = Transformations.map(relativeResponseLiveData) {
        when (it) {
            is Response.Success -> it.response.videoDetailResponse.items[0]
            is Response.Error -> null
        }
    }
    val isFavorite: MutableLiveData<Boolean> = MutableLiveData()
    val channel: LiveData<ChannelResponse.Channel> = Transformations.map(relativeResponseLiveData) {
        when (it) {
            is Response.Success -> it.response.channelResponse.items[0]
            is Response.Error -> null
        }
    }

    override val error: LiveData<Throwable> = Transformations.map(relativeResponseLiveData) {
        when (it) {
            is Response.Error -> it.throwable
            else -> null
        }
    }

    override val isShowError: LiveData<Boolean> = Transformations.map(relativeResponseLiveData) {
        it is Response.Error
    }
    override val progress: LiveData<Boolean> = youtubeRepository.isProgressLiveData

    fun checkFavorite() {
        executeCheckFavorite(videoId)
    }

    fun loadVideoAndChannelDetail(key: String) {
        executeLoadDetail(key, videoId, channelId)
    }

    fun registerFavorite() {
        val detail = videoDetail.value ?: return
        executeRegisterFavorite(detail)
    }

    fun unregisterFavorite() {
        executeUnregisterFavorite(videoId)
    }

    private fun executeCheckFavorite(videoId: String) = async(job + UI) {
        try {
            val isFind = favoriteRepository.existFavoriteVideo(videoId).await()
            isFavorite.postValue(isFind)
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    private fun executeLoadDetail(key: String, id: String, channelId: String) {
        val request = RelativeRequest(key, id, channelId)
        relativeRequestLiveData.postValue(request)
    }

    private fun executeRegisterFavorite(video: VideoDetailResponse.VideoDetail) = async(job + UI) {
        try {
            val favoriteVideo = FavoriteVideo(
                    video.id,
                    video.snippet.publishedAt,
                    video.snippet.title,
                    video.snippet.channelId,
                    video.snippet.channelTitle,
                    video.snippet.thumbnails.high.url)
            favoriteRepository.createFavoriteVideo(favoriteVideo).await()
            isFavorite.postValue(true)
        } catch (t: Throwable) {
            // TODO エラー処理
        }
    }

    private fun executeUnregisterFavorite(videoId: String) = async(job + UI) {
        try {
            favoriteRepository.deleteFavoriteVideo(videoId).await()
            isFavorite.postValue(false)
        } catch (t: Throwable) {
            t.printStackTrace()
            // TODO エラー処理
        }
    }
}