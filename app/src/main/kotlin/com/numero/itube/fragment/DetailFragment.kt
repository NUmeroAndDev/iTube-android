package com.numero.itube.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.numero.itube.R
import com.numero.itube.contract.DetailContract
import com.numero.itube.model.Video
import com.numero.itube.model.VideoDetail
import com.numero.itube.presenter.DetailPresenter
import com.numero.itube.repository.YoutubeRepository
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_detail.*
import javax.inject.Inject

class DetailFragment : Fragment(), DetailContract.View {

    @Inject
    lateinit var youtubeRepository: YoutubeRepository
    private lateinit var presenter: DetailContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)

        DetailPresenter(this, youtubeRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val arguments = arguments ?: return
        val video = arguments.getSerializable(ARG_VIDEO) as Video
        presenter.loadDetail(getString(R.string.api_key), video)
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe()
    }

    override fun onPause() {
        super.onPause()
        presenter.unSubscribe()
    }

    override fun showVideoDetail(videoDetail: VideoDetail) {
        titleTextView.text = videoDetail.snippet.title
        descriptionTextView.text = videoDetail.snippet.description
    }

    override fun showErrorMessage(e: Throwable?) {
        e?.printStackTrace()
    }

    override fun showProgress() {

    }

    override fun dismissProgress() {

    }

    override fun setPresenter(presenter: DetailContract.Presenter) {
        this.presenter = presenter
    }

    companion object {
        private const val ARG_VIDEO = "ARG_VIDEO"

        fun newInstance(video: Video): DetailFragment = DetailFragment().apply {
            arguments = bundleOf(ARG_VIDEO to video)
        }
    }
}