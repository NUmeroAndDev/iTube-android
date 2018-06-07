package com.numero.itube.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.LinearLayoutManager
import com.numero.itube.R
import com.numero.itube.contract.RelativeFavoriteContract
import com.numero.itube.extension.component
import com.numero.itube.presenter.RelativeFavoritePresenter
import com.numero.itube.repository.FavoriteVideoRepository
import com.numero.itube.repository.YoutubeRepository
import com.numero.itube.repository.db.FavoriteVideo
import com.numero.itube.view.adapter.RelativeFavoriteVideoListAdapter
import kotlinx.android.synthetic.main.fragment_relative_favorite.*
import javax.inject.Inject

class RelativeFavoriteFragment : BaseRelativeFragment(), RelativeFavoriteContract.View {

    @Inject
    lateinit var youtubeRepository: YoutubeRepository
    @Inject
    lateinit var favoriteRepository: FavoriteVideoRepository

    private lateinit var presenter: RelativeFavoriteContract.Presenter
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

        RelativeFavoritePresenter(this, youtubeRepository, favoriteRepository, videoId, channelId)
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
            presenter.loadDetail(getString(R.string.api_key))
        }

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

    override fun showVideoList(videoList: List<FavoriteVideo>) {
        videoListAdapter.videoList = videoList
    }

    override fun showErrorMessage(e: Throwable?) {
        errorGroup.visibility = View.VISIBLE
    }

    override fun hideErrorMessage() {
        errorGroup.visibility = View.GONE
    }

    override fun showProgress() {
        progressView?.show()
    }

    override fun dismissProgress() {
        progressView?.hide()
    }

    override fun setPresenter(presenter: RelativeFavoriteContract.Presenter) {
        this.presenter = presenter
    }

    fun playNextVideo() {
        videoListAdapter.playNextVideo()
    }

    override fun setIsRegistered(isRegistered: Boolean) {
        if (isRegistered) {
            presenter.registerFavorite()
        } else {
            presenter.unregisterFavorite()
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