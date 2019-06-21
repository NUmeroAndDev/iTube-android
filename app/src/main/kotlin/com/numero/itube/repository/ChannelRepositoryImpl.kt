package com.numero.itube.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.numero.itube.api.response.Result
import com.numero.itube.data.YoutubeDataSource
import com.numero.itube.model.BannerUrl
import com.numero.itube.model.ChannelDetail
import com.numero.itube.model.ChannelId
import com.numero.itube.model.ThumbnailUrl
import kotlinx.coroutines.Dispatchers

class ChannelRepositoryImpl(
        private val youtubeDataSource: YoutubeDataSource
) : ChannelRepository {

    override fun fetchChannelDetail(channelId: ChannelId): LiveData<Result<ChannelDetail>> = liveData(Dispatchers.IO) {
        val result = youtubeDataSource.getChannelDetail(channelId)
        when (result) {
            is Result.Error -> emit(Result.Error<ChannelDetail>(result.throwable))
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