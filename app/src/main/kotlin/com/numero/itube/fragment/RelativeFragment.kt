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
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.extension.component
import com.numero.itube.extension.observeNonNull
import com.numero.itube.repository.FavoriteVideoRepository
import com.numero.itube.repository.YoutubeRepository
import com.numero.itube.view.adapter.RelativeVideoListAdapter
import com.numero.itube.viewmodel.RelativeViewModel
import com.numero.itube.viewmodel.factory.RelativeViewModelFactory
import kotlinx.android.synthetic.main.fragment_relative.*
import javax.inject.Inject

class RelativeFragment : BaseRelativeFragment() {

    @Inject
    lateinit var youtubeRepository: YoutubeRepository
    @Inject
    lateinit var favoriteVideoRepository: FavoriteVideoRepository

    private lateinit var viewModel: RelativeViewModel
    private val videoListAdapter: RelativeVideoListAdapter = RelativeVideoListAdapter()
    private var listener: RelativeFragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is RelativeFragmentListener) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component?.inject(this)

        val arguments = arguments ?: return
        val videoId = arguments.getString(ARG_VIDEO_ID)
        val channelId = arguments.getString(ARG_CHANNEL_ID)

        val factory = RelativeViewModelFactory(youtubeRepository, favoriteVideoRepository, videoId, channelId)
        viewModel = ViewModelProviders.of(this, factory).get(RelativeViewModel::class.java)

        viewModel.videoList.observeNonNull(this) {
            videoListAdapter.videoList = it
        }

        viewModel.channel.observeNonNull(this) {
            showChannelDetail(it, channelId)
        }

        viewModel.videoDetail.observeNonNull(this) {
            showVideoDetail(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_relative, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoListAdapter.setOnItemClickListener {
            // 再生画面へ遷移
            listener?.showVideo(it)
        }
        relativeVideoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = videoListAdapter
        }

        retryButton.setOnClickListener {
            viewModel.loadVideoAndChannelDetail(getString(R.string.api_key))
        }

        viewModel.checkFavorite()
        viewModel.loadVideoAndChannelDetail(getString(R.string.api_key))
    }

//    override fun showErrorMessage(e: Throwable?) {
//        errorGroup.visibility = View.VISIBLE
//    }
//
//    override fun hideErrorMessage() {
//        errorGroup.visibility = View.GONE
//    }

    override fun setIsRegistered(isRegistered: Boolean) {
        if (isRegistered) {
            viewModel.registerFavorite()
        } else {
            viewModel.unregisterFavorite()
        }
    }

    interface RelativeFragmentListener : BaseRelativeFragmentListener {
        fun showVideo(video: SearchResponse.Video)
    }

    companion object {
        private const val ARG_VIDEO_ID = "ARG_VIDEO_ID"
        private const val ARG_CHANNEL_ID = "ARG_CHANNEL_ID"

        fun newInstance(videoId: String, channelId: String): RelativeFragment = RelativeFragment().apply {
            arguments = bundleOf(
                    ARG_VIDEO_ID to videoId,
                    ARG_CHANNEL_ID to channelId)
        }
    }
}