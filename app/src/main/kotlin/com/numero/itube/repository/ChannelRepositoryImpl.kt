package com.numero.itube.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.numero.itube.api.YoutubeApi
import com.numero.itube.api.response.Result
import com.numero.itube.extension.executeSync
import com.numero.itube.model.BannerUrl
import com.numero.itube.model.ChannelDetail
import com.numero.itube.model.ChannelId
import com.numero.itube.model.ThumbnailUrl

class ChannelRepositoryImpl(
        private val youtubeApi: YoutubeApi
) : ChannelRepository {

    override fun fetchChannelDetail(key: String, channelId: String): LiveData<Result<ChannelDetail>> = liveData {
        val result = youtubeApi.channelDetail(key, channelId).executeSync()
        when (result) {
            is Result.Error -> emit(Result.Error(result.throwable))
            is Result.Success -> {
                val detail = result.response.items[0]
                emit(Result.Success(
                        ChannelDetail(
                                ChannelId(detail.id),
                                detail.snippet.title,
                                ThumbnailUrl(detail.snippet.thumbnails.high.url),
                                BannerUrl(detail.branding.image.bannerTvMediumImageUrl)
                        )
                ))
            }
        }
    }
}