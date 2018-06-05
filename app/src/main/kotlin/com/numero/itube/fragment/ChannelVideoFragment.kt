package com.numero.itube.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.numero.itube.R
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.contract.ChannelDetailContract
import com.numero.itube.extension.component
import com.numero.itube.model.Thumbnail
import com.numero.itube.presenter.ChannelDetailPresenter
import com.numero.itube.repository.YoutubeRepository
import com.numero.itube.view.EndlessScrollListener
import com.numero.itube.view.adapter.VideoListAdapter
import kotlinx.android.synthetic.main.fragment_channel_video.*
import javax.inject.Inject

class ChannelVideoFragment : Fragment(), ChannelDetailContract.View {

    @Inject
    lateinit var youtubeApiRepository: YoutubeRepository

    private var listener: ChannelVideoFragmentListener? = null
    private lateinit var presenter: ChannelDetailContract.Presenter
    private val videoListAdapter: VideoListAdapter = VideoListAdapter()
    private var nextPageToken: String? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is ChannelVideoFragmentListener) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component?.inject(this)

        val arguments = arguments ?: return
        val channelId = arguments.getString(ARG_CHANNEL_ID)

        ChannelDetailPresenter(this, youtubeApiRepository, channelId)
        presenter.loadChannelDetail(getString(R.string.api_key))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_channel_video, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videoListAdapter.setOnItemClickListener {
            listener?.showVideo(it)
        }
        videoRecyclerView.apply {
            val manager = LinearLayoutManager(context)
            layoutManager = manager
            addOnScrollListener(EndlessScrollListener(manager) {
                val nextPageToken = nextPageToken ?: return@EndlessScrollListener
                presenter.loadNextVideo(getString(R.string.api_key), nextPageToken)
            })
            adapter = videoListAdapter
        }

        presenter.loadChannelDetail(getString(R.string.api_key))
    }

    override fun showChannelThumbnail(thumbnail: Thumbnail) {
    }

    override fun showVideoList(videoList: List<SearchResponse.Video>, nextPageToken: String?) {
        this.nextPageToken = nextPageToken
        videoListAdapter.videoList = videoList.toMutableList()
    }

    override fun showAddedVideoList(videoList: List<SearchResponse.Video>, nextPageToken: String?) {
        this.nextPageToken = nextPageToken
        videoListAdapter.addVideoList(videoList)
    }

    override fun showErrorMessage(e: Throwable?) {
    }

    override fun showProgress() {
        progressView.show()
    }

    override fun dismissProgress() {
        progressView.hide()
    }

    override fun setPresenter(presenter: ChannelDetailContract.Presenter) {
        this.presenter = presenter
    }

    interface ChannelVideoFragmentListener {
        fun showChannelThumbnail(urlString: String)

        fun showVideo(video: SearchResponse.Video)
    }

    companion object {
        private const val ARG_CHANNEL_ID = "ARG_CHANNEL_ID"

        fun newInstance(channelId: String): ChannelVideoFragment = ChannelVideoFragment().apply {
            arguments = bundleOf(ARG_CHANNEL_ID to channelId)
        }
    }
}