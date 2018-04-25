package com.numero.itube.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.numero.itube.R
import com.numero.itube.contract.RelativeContract
import com.numero.itube.model.Video
import com.numero.itube.presenter.RelativePresenter
import com.numero.itube.repository.YoutubeRepository
import com.numero.itube.view.adapter.RelativeVideoListAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_relative.*
import javax.inject.Inject

class RelativeFragment : Fragment(), RelativeContract.View {

    @Inject
    lateinit var youtubeRepository: YoutubeRepository

    private lateinit var presenter: RelativeContract.Presenter
    private val videoListAdapter: RelativeVideoListAdapter = RelativeVideoListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)

        RelativePresenter(this, youtubeRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_relative, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        videoListAdapter.setOnItemClickListener {
            // 再生画面へ遷移
        }
        relativeVideoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = videoListAdapter
        }

        val arguments = arguments ?: return
        val video = arguments.getSerializable(ARG_VIDEO) as Video
        presenter.loadRelative(getString(R.string.api_key), video)
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe()
    }

    override fun onPause() {
        super.onPause()
        presenter.unSubscribe()
    }

    override fun showVideoList(videoList: List<Video>) {
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

    companion object {
        private const val ARG_VIDEO = "ARG_VIDEO"

        fun newInstance(video: Video): RelativeFragment = RelativeFragment().apply {
            arguments = bundleOf(ARG_VIDEO to video)
        }
    }
}