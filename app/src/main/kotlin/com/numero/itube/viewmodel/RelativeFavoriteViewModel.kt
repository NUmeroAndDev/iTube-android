package com.numero.itube.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.numero.itube.api.request.ChannelRequest
import com.numero.itube.api.request.VideoDetailRequest
import com.numero.itube.api.response.ChannelResponse
import com.numero.itube.api.response.VideoDetailResponse
import com.numero.itube.repository.IFavoriteVideoRepository
import com.numero.itube.repository.IYoutubeRepository
import com.numero.itube.repository.db.FavoriteVideo
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async

class RelativeFavoriteViewModel(
        private val youtubeRepository: IYoutubeRepository,
        private val favoriteRepository: IFavoriteVideoRepository,
        private val videoId: String,
        private val channelId: String
) : ViewModel(), IErrorViewModel, IProgressViewModel {

    private val job = Job()

    val videoList: MutableLiveData<List<FavoriteVideo>> = MutableLiveData()
    val nextPageToken: MutableLiveData<String> = MutableLiveData()
    val videoDetail: MutableLiveData<VideoDetailResponse.VideoDetail> = MutableLiveData()
    val isFavorite: MutableLiveData<Boolean> = MutableLiveData()
    val channel: MutableLiveData<ChannelResponse.Channel> = MutableLiveData()
    override val error: MutableLiveData<Throwable> = MutableLiveData()
    override val progress: MutableLiveData<Boolean> = MutableLiveData()

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

    private fun executeLoadDetail(key: String, id: String, channelId: String) = async(job + UI) {
        progress.postValue(true)
        try {
            val detailRequest = VideoDetailRequest(key, id)
            val videoDetailResponse = youtubeRepository.loadDetail(detailRequest).await()
            videoDetail.postValue(videoDetailResponse.items[0])

            val channelRequest = ChannelRequest(key, channelId)
            val channelResponse = youtubeRepository.loadChannel(channelRequest).await()

            val favoriteVideoList = favoriteRepository.loadFavoriteVideo().await()

            videoList.postValue(favoriteVideoList)
            channel.postValue(channelResponse.items[0])
        } catch (t: Throwable) {
            error.postValue(t)
        } finally {
            progress.postValue(false)
        }
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