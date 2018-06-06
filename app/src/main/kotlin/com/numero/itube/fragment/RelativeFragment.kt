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
import com.numero.itube.contract.RelativeContract
import com.numero.itube.extension.component
import com.numero.itube.extension.findFragment
import com.numero.itube.extension.replace
import com.numero.itube.presenter.RelativePresenter
import com.numero.itube.repository.YoutubeRepository
import com.numero.itube.view.adapter.RelativeVideoListAdapter
import kotlinx.android.synthetic.main.fragment_relative.*
import javax.inject.Inject

class RelativeFragment : Fragment(), RelativeContract.View {

    @Inject
    lateinit var youtubeRepository: YoutubeRepository

    private lateinit var presenter: RelativeContract.Presenter
    private val videoListAdapter: RelativeVideoListAdapter = RelativeVideoListAdapter()
    private var listener: RelativeFragmentListener? = null
    private lateinit var videoId: String
    private lateinit var channelId: String

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
        videoId = arguments.getString(ARG_VIDEO_ID)
        channelId = arguments.getString(ARG_CHANNEL_ID)

        RelativePresenter(this, youtubeRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_relative, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (findFragment(R.id.detailContainer) == null) {
            replace(R.id.detailContainer, VideoDetailFragment.newInstance(videoId, channelId))
        }

        videoListAdapter.setOnItemClickListener {
            // 再生画面へ遷移
            listener?.showVideo(it)
        }
        relativeVideoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = videoListAdapter
        }

        presenter.loadRelative(getString(R.string.api_key), videoId)
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe()
    }

    override fun onPause() {
        super.onPause()
        presenter.unSubscribe()
    }

    override fun showVideoList(videoList: List<SearchResponse.Video>) {
        videoListAdapter.videoList = videoList
    }

    override fun showErrorMessage(e: Throwable?) {
        e?.printStackTrace()
    }

    override fun showProgress() {

    }

    override fun dismissProgress() {

    }

    override fun setPresenter(presenter: RelativeContract.Presenter) {
        this.presenter = presenter
    }

    interface RelativeFragmentListener {
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