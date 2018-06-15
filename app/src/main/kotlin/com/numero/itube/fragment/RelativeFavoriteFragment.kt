package com.numero.itube.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.numero.itube.R
import com.numero.itube.extension.component
import com.numero.itube.extension.observeNonNull
import com.numero.itube.repository.FavoriteVideoRepository
import com.numero.itube.repository.YoutubeRepository
import com.numero.itube.repository.db.FavoriteVideo
import com.numero.itube.view.adapter.RelativeFavoriteVideoListAdapter
import com.numero.itube.viewmodel.RelativeFavoriteViewModel
import com.numero.itube.viewmodel.factory.RelativeFavoriteViewModelFactory
import kotlinx.android.synthetic.main.fragment_relative_favorite.*
import javax.inject.Inject

class RelativeFavoriteFragment : BaseRelativeFragment() {

    @Inject
    lateinit var youtubeRepository: YoutubeRepository
    @Inject
    lateinit var favoriteVideoRepository: FavoriteVideoRepository


    private lateinit var viewModel: RelativeFavoriteViewModel
    private val videoListAdapter: RelativeFavoriteVideoListAdapter = RelativeFavoriteVideoListAdapter()
    private var listener: RelativeFavoriteFragmentListener? = null
    private lateinit var videoId: String

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is RelativeFavoriteFragmentListener) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component?.inject(this)

        val arguments = arguments ?: return
        videoId = arguments.getString(ARG_VIDEO_ID)
        val channelId = arguments.getString(ARG_CHANNEL_ID)

        val factory = RelativeFavoriteViewModelFactory(youtubeRepository, favoriteVideoRepository, videoId, channelId)
        viewModel = ViewModelProviders.of(this, factory).get(RelativeFavoriteViewModel::class.java)

        viewModel.videoList.observeNonNull(this) {
            videoListAdapter.videoList = it
        }

        viewModel.progress.observeNonNull(this) {
            if (it) {
                progressView.show()
            } else {
                progressView.hide()
            }
        }

        viewModel.channel.observeNonNull(this) {
            showChannelDetail(it, channelId)
        }

        viewModel.videoDetail.observeNonNull(this) {
            showVideoDetail(it)
        }

        viewModel.isFavorite.observeNonNull(this) {
            registeredFavorite(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_relative_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoListAdapter.apply {
            setOnItemClickListener {
                // 再生画面へ遷移
                listener?.showVideo(it)
            }
            currentVideoId = videoId
        }
        favoriteVideoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = videoListAdapter
        }

        retryButton.setOnClickListener {
            viewModel.loadVideoAndChannelDetail(getString(R.string.api_key))
        }

        viewModel.loadVideoAndChannelDetail(getString(R.string.api_key))
        viewModel.checkFavorite()
    }

//    override fun showErrorMessage(e: Throwable?) {
//        errorGroup.visibility = View.VISIBLE
//    }
//
//    override fun hideErrorMessage() {
//        errorGroup.visibility = View.GONE
//    }

    fun playNextVideo() {
        videoListAdapter.playNextVideo()
    }

    override fun setIsRegistered(isRegistered: Boolean) {
        if (isRegistered) {
            viewModel.registerFavorite()
        } else {
            viewModel.unregisterFavorite()
        }
    }

    interface RelativeFavoriteFragmentListener : BaseRelativeFragment.BaseRelativeFragmentListener {
        fun showVideo(video: FavoriteVideo)
    }

    companion object {
        private const val ARG_VIDEO_ID = "ARG_VIDEO_ID"
        private const val ARG_CHANNEL_ID = "ARG_CHANNEL_ID"

        fun newInstance(videoId: String, channelId: String): RelativeFavoriteFragment = RelativeFavoriteFragment().apply {
            arguments = bundleOf(
                    ARG_VIDEO_ID to videoId,
                    ARG_CHANNEL_ID to channelId)
        }
    }
}