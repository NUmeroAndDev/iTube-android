package com.numero.itube.ui.video

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.numero.itube.R
import com.numero.itube.activity.ChannelDetailActivity
import com.numero.itube.extension.component
import com.numero.itube.extension.getAttrColor
import com.numero.itube.extension.getTintedDrawable
import com.numero.itube.fragment.FavoriteListBottomSheetFragment
import com.numero.itube.model.ChannelId
import com.numero.itube.model.Video
import com.numero.itube.model.VideoId
import com.numero.itube.repository.ConfigRepository
import javax.inject.Inject

class VideoDetailActivity : AppCompatActivity(),
        YouTubePlayer.OnInitializedListener,
        YouTubePlayer.PlayerStateChangeListener {

    private val videoId: VideoId by lazy {
        val id = intent.getStringExtra(BUNDLE_VIDEO_ID)
        VideoId(id)
    }
    private val channelId: ChannelId by lazy {
        val id = intent.getStringExtra(BUNDLE_CHANNEL_ID)
        ChannelId(id)
    }
    private var player: YouTubePlayer? = null


    @Inject
    lateinit var configRepository: ConfigRepository

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(VideoDetailViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component?.inject(this)
        setContentView(R.layout.activity_video_detail)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            val colorOnPrimary = getAttrColor(R.attr.colorOnPrimary)
            val drawable = getTintedDrawable(R.drawable.ic_arrow_back, colorOnPrimary) ?: return
            setHomeAsUpIndicator(drawable)

            title = this@VideoDetailActivity.title
        }

        initViews()
        setupObserve()

        val youTubePlayerFragment = YouTubePlayerFragment.newInstance().apply {
            this@VideoDetailActivity.fragmentManager.beginTransaction().replace(R.id.playerContainer, this).commit()
//            replace(R.id.playerContainer, this, false)
        }
        youTubePlayerFragment.initialize(configRepository.apiKey, this)

        viewModel.executeLoadVideoDetail(videoId, channelId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onInitializationSuccess(p0: YouTubePlayer.Provider?, youTubePlayer: YouTubePlayer?, b: Boolean) {
        player = youTubePlayer?.apply {
            setPlayerStateChangeListener(this@VideoDetailActivity)
        }
        if (b.not()) {
            player?.loadVideo(videoId.value)
        }
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
    }

    override fun onAdStarted() {
    }

    override fun onLoading() {
    }

    override fun onVideoStarted() {
    }

    override fun onLoaded(p0: String?) {
    }

    override fun onVideoEnded() {
        if (configRepository.isLoop) {
            player?.play()
            return
        }
        //val nextVideo = viewModel.nextFavoriteVideo ?: return
        //showVideo(nextVideo)
    }

    override fun onError(p0: YouTubePlayer.ErrorReason?) {
    }

    private fun setupObserve() {
        viewModel.videoDetailLiveData.observe(this) {
            Log.d("Log", it.toString())
        }
    }

    private fun initViews() {
        // TODO
    }

    private fun showVideo(video: Video.Search) {
        startActivity(VideoDetailActivity.createIntent(this, video))
        overridePendingTransition(0, 0)
    }

    private fun showChannelDetailScreen(channelName: String, channelId: String, thumbnailUrl: String, transitionView: Pair<View, String>) {
        val bundle = ActivityOptions.makeSceneTransitionAnimation(this, transitionView).toBundle()
        startActivity(ChannelDetailActivity.createIntent(this, channelName, channelId, thumbnailUrl), bundle)
    }

    private fun showFavoriteList() {
        FavoriteListBottomSheetFragment.newInstance(videoId.value).show(supportFragmentManager)
    }

    companion object {

        private const val BUNDLE_VIDEO_ID = "BUNDLE_VIDEO_ID"
        private const val BUNDLE_CHANNEL_ID = "BUNDLE_CHANNEL_ID"

        fun createIntent(context: Context, video: Video.Search): Intent = Intent(context, VideoDetailActivity::class.java).apply {
            putExtra(BUNDLE_VIDEO_ID, video.id.value)
            putExtra(BUNDLE_CHANNEL_ID, video.channel.id.value)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}