package com.numero.itube.ui.video.detail.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.numero.itube.R
import com.numero.itube.extension.component
import com.numero.itube.model.ChannelId
import com.numero.itube.model.VideoDetail
import com.numero.itube.model.VideoId
import com.numero.itube.ui.video.detail.item.ChannelItem
import com.numero.itube.ui.video.detail.item.PlaylistVideoItem
import com.numero.itube.ui.video.detail.item.VideoDetailItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_detail.*
import javax.inject.Inject

class DetailInSearchFragment : Fragment() {

    private val videoId: VideoId by lazy {
        val id = arguments?.getString(ARG_VIDEO_ID)
        VideoId(requireNotNull(id))
    }
    private val channelId: ChannelId by lazy {
        val id = arguments?.getString(ARG_CHANNEL_ID)
        ChannelId(requireNotNull(id))
    }

    private val groupieAdapter = GroupAdapter<ViewHolder>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(DetailInSearchViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component?.inject(this)
        setupObserve()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
        viewModel.executeLoadVideoDetail(videoId, channelId)
    }

    private fun setupViews() {
        detailRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = groupieAdapter
        }
    }

    private fun setupObserve() {
        viewModel.videoDetailLiveData.observe(this) {
            groupieAdapter.clear()
            groupieAdapter.add(it.toSection())
        }
    }

    private fun VideoDetail.toSection(): Section {
        return Section().apply {
            add(VideoDetailItem(this@toSection))
            add(ChannelItem(channelDetail))
            addAll(relativeVideoList.map { PlaylistVideoItem(it) })
        }
    }

    companion object {

        private const val ARG_VIDEO_ID = "ARG_VIDEO_ID"
        private const val ARG_CHANNEL_ID = "ARG_CHANNEL_ID"

        fun newInstance(videoId: VideoId, channelId: ChannelId): DetailInSearchFragment = DetailInSearchFragment().apply {
            arguments = bundleOf(
                    ARG_VIDEO_ID to videoId.value,
                    ARG_CHANNEL_ID to channelId.value
            )
        }
    }
}