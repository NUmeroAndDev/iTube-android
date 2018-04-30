package com.numero.itube.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.numero.itube.R
import com.numero.itube.contract.RelativeFavoriteContract
import com.numero.itube.presenter.RelativeFavoritePresenter
import com.numero.itube.repository.FavoriteVideoRepository
import com.numero.itube.repository.db.FavoriteVideo
import com.numero.itube.view.adapter.RelativeFavoriteVideoListAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_relative_favorite.*
import javax.inject.Inject

class RelativeFavoriteFragment : Fragment(), RelativeFavoriteContract.View {

    @Inject
    lateinit var favoriteRepository: FavoriteVideoRepository

    private lateinit var presenter: RelativeFavoriteContract.Presenter
    private val videoListAdapter: RelativeFavoriteVideoListAdapter = RelativeFavoriteVideoListAdapter()
    private var listener: RelativeFavoriteFragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is RelativeFavoriteFragmentListener) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)

        RelativeFavoritePresenter(this, favoriteRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_relative_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arguments = arguments ?: return
        val videoId = arguments.getString(ARG_VIDEO_ID)

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

        presenter.loadFavoriteList()
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
        e?.printStackTrace()
    }

    override fun showProgress() {

    }

    override fun dismissProgress() {

    }

    override fun setPresenter(presenter: RelativeFavoriteContract.Presenter) {
        this.presenter = presenter
    }

    interface RelativeFavoriteFragmentListener {
        fun showVideo(video: FavoriteVideo)
    }

    companion object {
        private const val ARG_VIDEO_ID = "ARG_VIDEO_ID"

        fun newInstance(videoId: String): RelativeFavoriteFragment = RelativeFavoriteFragment().apply {
            arguments = bundleOf(ARG_VIDEO_ID to videoId)
        }
    }
}