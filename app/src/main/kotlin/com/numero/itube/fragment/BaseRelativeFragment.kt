package com.numero.itube.fragment

import android.content.Context
import android.util.Pair
import android.view.View
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.numero.itube.api.response.ChannelResponse
import com.numero.itube.api.response.VideoDetailResponse
import com.numero.itube.contract.BaseRelativeContract
import kotlinx.android.synthetic.main.container_video_detail.*

/**
 * ここはVideoDetail系の処理をまとめるためだけのFragment
 */
open class BaseRelativeFragment : Fragment(), BaseRelativeContract.IBaseRelativeView {

    private var listener: BaseRelativeFragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is BaseRelativeFragmentListener) {
            listener = context
        }
    }

    override fun showVideoDetail(videoDetail: VideoDetailResponse.VideoDetail, channel: ChannelResponse.Channel, channelId: String) {
        val context = context ?: return

        descriptionTextView.text = videoDetail.snippet.description
        channelNameTextView.text = channel.snippet.title

        val url = channel.snippet.thumbnails.medium.url
        Glide.with(context).load(url).apply(RequestOptions().circleCrop()).into(channelImageView)
        channelLayout.setOnClickListener {
            val channelName = channelNameTextView.text.toString()
            listener?.onClickChannel(
                    channelName,
                    channelId,
                    url,
                    Pair(channelImageView, channelImageView.transitionName),
                    Pair(channelNameTextView, channelNameTextView.transitionName))
        }
    }

    fun showChannelDetail(channel: ChannelResponse.Channel, channelId: String) {
        val context = context ?: return

        channelNameTextView.text = channel.snippet.title

        val url = channel.snippet.thumbnails.medium.url
        Glide.with(context).load(url).apply(RequestOptions().circleCrop()).into(channelImageView)
        channelLayout.setOnClickListener {
            val channelName = channelNameTextView.text.toString()
            listener?.onClickChannel(
                    channelName,
                    channelId,
                    url,
                    Pair(channelImageView, channelImageView.transitionName),
                    Pair(channelNameTextView, channelNameTextView.transitionName))
        }
    }

    fun showVideoDetail(videoDetail: VideoDetailResponse.VideoDetail) {
        descriptionTextView.text = videoDetail.snippet.description
    }

    override fun registeredFavorite(isRegistered: Boolean) {
        listener?.onIsRegisteredFavorite(isRegistered)
    }

    override fun setIsRegistered(isRegistered: Boolean) {
        //継承先で使用するためここでは使わない
    }

    interface BaseRelativeFragmentListener {
        fun onIsRegisteredFavorite(isRegisteredFavorite: Boolean)

        fun onClickChannel(channelName: String, channelId: String, thumbnailUrl: String, vararg transitionViews: Pair<View, String>)
    }
}