package com.numero.itube.ui.video.detail.playlist

import android.content.Context
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
import com.numero.itube.model.*
import com.numero.itube.ui.video.SelectPlaylistBottomSheetFragment
import com.numero.itube.ui.video.detail.DetailCallback
import com.numero.itube.ui.video.detail.item.ChannelItem
import com.numero.itube.ui.video.detail.item.PlaylistVideoItem
import com.numero.itube.ui.video.detail.item.VideoDetailItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_detail.*
import javax.inject.Inject

class DetailInPlaylistFragment : Fragment(),
        SelectPlaylistBottomSheetFragment.SelectPlaylistListener {

    private val videoId: VideoId by lazy {
        val id = arguments?.getString(ARG_VIDEO_ID)
        VideoId(requireNotNull(id))
    }
    private val channelId: ChannelId by lazy {
        val id = arguments?.getString(ARG_CHANNEL_ID)
        ChannelId(requireNotNull(id))
    }
    private val playlistId: PlaylistId by lazy {
        val id = arguments?.getLong(ARG_PLAYLIST_ID)
        PlaylistId(requireNotNull(id))
    }

    private val groupieAdapter = GroupAdapter<ViewHolder>()

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(DetailInPlaylistViewModel::class.java)
    }

    private var callback: DetailCallback? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is DetailCallback) {
            callback = context
        }
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
        viewModel.executeLoadVideoDetail(videoId, channelId, playlistId)
    }

    override fun onSelectedPlaylist(playlist: Playlist, videoId: VideoId) {
        val videoDetail = viewModel.videoDetailLiveData.value?.first ?: return
        callback?.addPlaylist(playlist, videoDetail)
    }

    private fun setupViews() {
        groupieAdapter.setOnItemClickListener { item, view ->
            when (item) {
                is PlaylistVideoItem -> callback?.showVideo(item.video)
                is ChannelItem -> callback?.showChannelDetail(item.channelDetail)
            }
        }
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

    private fun showSelectPlaylist() {
        SelectPlaylistBottomSheetFragment.newInstance(this, videoId).show(requireFragmentManager())
    }

    private fun Pair<VideoDetail, PlaylistDetail>.toSection(): Section {
        return Section().apply {
            add(VideoDetailItem(first, true) {
                showSelectPlaylist()
            })
            add(ChannelItem(first.channelDetail))
            addAll(second.videoList.map { PlaylistVideoItem(it) })
        }
    }

    companion object {

        private const val ARG_VIDEO_ID = "ARG_VIDEO_ID"
        private const val ARG_CHANNEL_ID = "ARG_CHANNEL_ID"
        private const val ARG_PLAYLIST_ID = "ARG_PLAYLIST_ID"

        fun newInstance(videoId: VideoId, channelId: ChannelId, playlistId: PlaylistId): DetailInPlaylistFragment = DetailInPlaylistFragment().apply {
            arguments = bundleOf(
                    ARG_VIDEO_ID to videoId.value,
                    ARG_CHANNEL_ID to channelId.value,
                    ARG_PLAYLIST_ID to playlistId.value
            )
        }
    }
}