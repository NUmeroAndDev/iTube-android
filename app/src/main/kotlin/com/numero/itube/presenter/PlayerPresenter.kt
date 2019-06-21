package com.numero.itube.presenter

import com.numero.itube.api.request.RelativeRequest
import com.numero.itube.api.response.Result
import com.numero.itube.api.response.VideoDetailResponse
import com.numero.itube.repository.IConfigRepository
import com.numero.itube.repository.IFavoriteVideoRepository
import com.numero.itube.repository.IYoutubeRepository
import com.numero.itube.repository.db.FavoriteVideo
import com.numero.itube.viewmodel.PlayerViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class PlayerPresenter(
        private val viewModel: PlayerViewModel,
        private val youtubeRepository: IYoutubeRepository,
        private val favoriteRepository: IFavoriteVideoRepository,
        private val configRepository: IConfigRepository,
        private val videoId: String,
        private val channelId: String
) : IPlayerPresenter {

    override fun subscribe() {
    }

    override fun unSubscribe() {
    }

    override fun checkFavorite() {
        GlobalScope.launch(Dispatchers.Main) {
            val isFavorite = async(Dispatchers.Default) { favoriteRepository.existFavoriteVideo(videoId) }.await()
            viewModel.isFavorite.postValue(isFavorite)
        }
    }

    override fun loadVideoAndChannelDetail() {
        if (viewModel.isProcessing) {
            return
        }

        viewModel.isShowProgress.postValue(true)
        viewModel.isShowError.postValue(false)

        val request = RelativeRequest(configRepository.apiKey, videoId, channelId)

//        GlobalScope.launch(Dispatchers.Main) {
//            val result = async(Dispatchers.Default) { youtubeRepository.loadRelative(request) }.await()
//            viewModel.isShowProgress.postValue(false)
//            viewModel.isShowError.postValue(result is Result.Error)
//            when (result) {
//                is Result.Error -> viewModel.error.postValue(result.throwable)
//                is Result.Success -> {
//                    viewModel.isShowError.postValue(false)
//                    viewModel.relativeResponse.postValue(result.response)
//                }
//            }
//        }
    }

    override fun loadNextFavoriteVideo(currentVideoId: String) {
        GlobalScope.launch(Dispatchers.Main) {
            val list = async(Dispatchers.Default) { favoriteRepository.loadFavoriteVideo() }.await()
            val position = list.value.indexOfFirst { it.id.value == currentVideoId }
            if (list.value.isNotEmpty()) {
                viewModel.nextFavoriteVideo = if (position == list.value.lastIndex) {
                    list.value.first()
                } else {
                    list.value[position + 1]
                }
            }
        }
    }

    override fun changeFavorite() {
        val isFavorite = viewModel.isFavorite.value ?: return
        if (isFavorite) {
            executeUnregisterFavorite(videoId)
        } else {
//            val response = viewModel.relativeResponse.value ?: return
//            val detail = response.videoDetailResponse.items[0]
//            executeRegisterFavorite(detail)
        }
    }

    private fun executeRegisterFavorite(video: VideoDetailResponse.VideoDetail) {
        val favoriteVideo = FavoriteVideo(
                video.id,
                video.snippet.publishedAt,
                video.snippet.title,
                video.snippet.channelId,
                video.snippet.channelTitle,
                video.snippet.thumbnails.high.url)
        GlobalScope.launch(Dispatchers.Main) {
            async(Dispatchers.Default) { favoriteRepository.createFavoriteVideo(favoriteVideo) }.await()
            viewModel.isFavorite.postValue(true)
        }
    }

    private fun executeUnregisterFavorite(videoId: String) {
        GlobalScope.launch(Dispatchers.Main) {
            async(Dispatchers.Default) { favoriteRepository.deleteFavoriteVideo(videoId) }.await()
            viewModel.isFavorite.postValue(false)
        }
    }
}