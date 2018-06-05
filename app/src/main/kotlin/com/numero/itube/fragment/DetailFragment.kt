package com.numero.itube.fragment

import android.content.Context
import android.os.Bundle
import android.util.Pair
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.numero.itube.R
import com.numero.itube.api.response.ChannelResponse
import com.numero.itube.api.response.VideoDetailResponse
import com.numero.itube.contract.DetailContract
import com.numero.itube.extension.component
import com.numero.itube.presenter.DetailPresenter
import com.numero.itube.repository.FavoriteVideoRepository
import com.numero.itube.repository.YoutubeRepository
import kotlinx.android.synthetic.main.fragment_detail.*
import javax.inject.Inject

class DetailFragment : Fragment(), DetailContract.View {

    @Inject
    lateinit var youtubeRepository: YoutubeRepository
    @Inject
    lateinit var favoriteVideoRepository: FavoriteVideoRepository

    private lateinit var presenter: DetailContract.Presenter
    private lateinit var channelId: String
    private var listener: DetailFragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is DetailFragmentListener) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component?.inject(this)

        val arguments = arguments ?: return
        val videoId = arguments.getString(ARG_VIDEO_ID)
        channelId = arguments.getString(ARG_CHANNEL_ID)
        DetailPresenter(this, youtubeRepository, favoriteVideoRepository, videoId, channelId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter.loadDetail(getString(R.string.api_key))
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe()
    }

    override fun onPause() {
        super.onPause()
        presenter.unSubscribe()
    }

    override fun showVideoDetail(videoDetail: VideoDetailResponse.VideoDetail, channel: ChannelResponse.Channel) {
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

    override fun showErrorMessage(e: Throwable?) {
        e?.printStackTrace()
    }

    override fun showProgress() {
        progressView?.show()
    }

    override fun dismissProgress() {
        progressView?.hide()
    }

    override fun registeredFavorite(isRegistered: Boolean) {
        listener?.onIsRegisteredFavorite(isRegistered)
    }

    override fun setPresenter(presenter: DetailContract.Presenter) {
        this.presenter = presenter
    }

    fun setIsRegistered(isRegistered: Boolean) {
        if (isRegistered) {
            presenter.registerFavorite()
        } else {
            presenter.unregisterFavorite()
        }
    }

    interface DetailFragmentListener {
        fun onIsRegisteredFavorite(isRegisteredFavorite: Boolean)

        fun onClickChannel(channelName: String, channelId: String, thumbnailUrl: String, vararg transitionViews: Pair<View, String>)
    }

    companion object {
        private const val ARG_VIDEO_ID = "ARG_VIDEO_ID"
        private const val ARG_CHANNEL_ID = "ARG_CHANNEL_ID"

        fun newInstance(videoId: String, channelId: String): DetailFragment = DetailFragment().apply {
            arguments = bundleOf(
                    ARG_VIDEO_ID to videoId,
                    ARG_CHANNEL_ID to channelId)
        }
    }
}