package com.numero.itube.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.numero.itube.R
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.contract.ChannelVideoListContract
import com.numero.itube.extension.component
import com.numero.itube.extension.observeNonNull
import com.numero.itube.presenter.ChannelVideoListPresenter
import com.numero.itube.repository.YoutubeRepository
import com.numero.itube.view.EndlessScrollListener
import com.numero.itube.view.adapter.VideoListAdapter
import com.numero.itube.viewmodel.ChannelVideoListViewModel
import kotlinx.android.synthetic.main.fragment_channel_video_list.*
import javax.inject.Inject

class ChannelVideoListFragment : Fragment() {

    @Inject
    lateinit var youtubeApiRepository: YoutubeRepository
    private lateinit var presenter: ChannelVideoListContract.Presenter
    private lateinit var viewModel: ChannelVideoListViewModel

    private var listener: ChannelVideoFragmentListener? = null
    private val videoListAdapter: VideoListAdapter = VideoListAdapter()

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

        viewModel = ViewModelProviders.of(this).get(ChannelVideoListViewModel::class.java)
        viewModel.videoList.observeNonNull(this) {
            videoListAdapter.submitList(it)
        }
        viewModel.progress.observeNonNull(this) {
            if (it) {
                progressView.show()
            } else {
                progressView.hide()
            }
        }

        presenter = ChannelVideoListPresenter(viewModel, channelId, youtubeApiRepository)

        if (savedInstanceState == null) {
            // 画面回転時には以前のデータが復帰される
            loadChannelVideo()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_channel_video_list, container, false)
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
                loadChannelVideo(viewModel.nextPageToken.value)
            })
            adapter = videoListAdapter
        }
    }

    private fun loadChannelVideo(nextPageToken: String? = null) {
        presenter.loadChannelVideo(getString(R.string.api_key), nextPageToken)
    }

    interface ChannelVideoFragmentListener {
        fun showChannelThumbnail(urlString: String)

        fun showVideo(video: SearchResponse.Video)
    }

    companion object {
        private const val ARG_CHANNEL_ID = "ARG_CHANNEL_ID"

        fun newInstance(channelId: String): ChannelVideoListFragment = ChannelVideoListFragment().apply {
            arguments = bundleOf(ARG_CHANNEL_ID to channelId)
        }
    }
}