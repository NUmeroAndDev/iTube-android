package com.numero.itube.ui.video

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.numero.itube.R
import com.numero.itube.extension.component
import com.numero.itube.model.Playlist
import com.numero.itube.model.PlaylistList
import com.numero.itube.model.VideoId
import com.numero.itube.ui.video.item.PlaylistItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_favorite_list.view.emptyMessageTextView
import kotlinx.android.synthetic.main.fragment_select_playlist.view.*
import javax.inject.Inject

class SelectPlaylistBottomSheetFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SelectPlaylistViewModel::class.java)
    }

    private val videoId: VideoId by lazy {
        val id = arguments?.getString(ARG_VIDEO_ID) as String
        VideoId(id)
    }

    private lateinit var emptyMessageTextView: TextView
    private val groupieAdapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component?.inject(this)

        setupObserve()
    }

    override fun onResume() {
        super.onResume()
        viewModel.executeLoadPlaylist()
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = View.inflate(context, R.layout.fragment_select_playlist, null)
        setupFavoriteVideoList(view)
        dialog.setContentView(view)
    }

    private fun setupObserve() {
        viewModel.playlistListLiveData.observe(this) {
            groupieAdapter.add(it.toSection())
            emptyMessageTextView.isVisible = it.value.isEmpty()
        }
    }

    private fun setupFavoriteVideoList(view: View) {
        groupieAdapter.setOnItemClickListener { item, _ ->
            if (item is PlaylistItem) {
                val fragment = targetFragment
                if (fragment is SelectPlaylistListener) {
                    fragment.onSelectedPlaylist(item.playlist, videoId)
                }
                dismiss()
            }
        }
        view.playlistRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = groupieAdapter
        }
        emptyMessageTextView = view.emptyMessageTextView
    }

    private fun PlaylistList.toSection(): Section {
        return Section().apply {
            addAll(value.map { PlaylistItem(it) })
        }
    }

    fun show(manager: FragmentManager) {
        if (manager.findFragmentByTag(TAG) != null) {
            return
        }
        show(manager, TAG)
    }

    interface SelectPlaylistListener {
        fun onSelectedPlaylist(playlist: Playlist, videoId: VideoId)
    }

    companion object {
        private const val TAG = "SelectPlaylistBottomSheetFragment"

        private const val ARG_VIDEO_ID = "ARG_VIDEO_ID"
        private const val FRAGMENT_REQUEST_CODE = 1

        fun newInstance(fragment: Fragment, videoId: VideoId): SelectPlaylistBottomSheetFragment = SelectPlaylistBottomSheetFragment().apply {
            setTargetFragment(fragment, FRAGMENT_REQUEST_CODE)
            arguments = bundleOf(ARG_VIDEO_ID to videoId.value)
        }
    }
}