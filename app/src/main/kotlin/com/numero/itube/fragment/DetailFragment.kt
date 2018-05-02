package com.numero.itube.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import com.bumptech.glide.request.RequestOptions
import com.numero.itube.GlideApp
import com.numero.itube.R
import com.numero.itube.contract.DetailContract
import com.numero.itube.model.Channel
import com.numero.itube.model.VideoDetail
import com.numero.itube.presenter.DetailPresenter
import com.numero.itube.repository.FavoriteVideoRepository
import com.numero.itube.repository.YoutubeRepository
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_detail.*
import javax.inject.Inject

class DetailFragment : Fragment(), DetailContract.View {

    @Inject
    lateinit var youtubeRepository: YoutubeRepository
    @Inject
    lateinit var favoriteVideoRepository: FavoriteVideoRepository

    private lateinit var presenter: DetailContract.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)

        val arguments = arguments ?: return
        val videoId = arguments.getString(ARG_VIDEO_ID)
        val channelId = arguments.getString(ARG_CHANNEL_ID)
        DetailPresenter(this, youtubeRepository, favoriteVideoRepository, videoId, channelId)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

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

    override fun showVideoDetail(videoDetail: VideoDetail, channel: Channel) {
        titleTextView.text = videoDetail.snippet.title
        descriptionTextView.text = videoDetail.snippet.description
        channelNameTextView.text = channel.snippet.title
        val context = context ?: return
        GlideApp.with(context).load(channel.snippet.thumbnails.medium.url).apply(RequestOptions().circleCrop()).into(channelImageView)
    }

    override fun showErrorMessage(e: Throwable?) {
        e?.printStackTrace()
    }

    override fun showProgress() {
        progressView?.show()
    }

    override fun dismissProgress() {
        progressView?.hide()
    }

    override fun registeredFavorite(isRegistered: Boolean) {
        favoriteCheckBox.isChecked = isRegistered
    }

    override fun setPresenter(presenter: DetailContract.Presenter) {
        this.presenter = presenter
    }

    private fun initViews() {
        favoriteCheckBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                presenter.registerFavorite()
            } else {
                presenter.unregisterFavorite()
            }
        }
    }

    companion object {
        private const val ARG_VIDEO_ID = "ARG_VIDEO_ID"
        private const val ARG_CHANNEL_ID = "ARG_CHANNEL_ID"

        fun newInstance(videoId: String, channelId: String): DetailFragment = DetailFragment().apply {
            arguments = bundleOf(
                    ARG_VIDEO_ID to videoId,
                    ARG_CHANNEL_ID to channelId)
        }
    }
}