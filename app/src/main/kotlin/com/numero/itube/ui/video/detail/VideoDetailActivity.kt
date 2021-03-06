package com.numero.itube.ui.video.detail

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.numero.itube.R
import com.numero.itube.ui.channel.ChannelDetailActivity
import com.numero.itube.extension.component
import com.numero.itube.extension.getAttrColor
import com.numero.itube.extension.getTintedDrawable
import com.numero.itube.extension.replace
import com.numero.itube.model.*
import com.numero.itube.repository.ConfigRepository
import com.numero.itube.ui.video.detail.playlist.DetailInPlaylistFragment
import com.numero.itube.ui.video.detail.search.DetailInSearchFragment
import javax.inject.Inject

class VideoDetailActivity : AppCompatActivity(),
        YouTubePlayer.OnInitializedListener,
        YouTubePlayer.PlayerStateChangeListener,
        DetailCallback {

    private val videoId: VideoId by lazy {
        val id = intent.getStringExtra(BUNDLE_VIDEO_ID)
        VideoId(id)
    }
    private val channelId: ChannelId by lazy {
        val id = intent.getStringExtra(BUNDLE_CHANNEL_ID)
        ChannelId(id)
    }
    private val playlistId: PlaylistId? by lazy {
        val notExistId = -1L
        val id = intent.getLongExtra(BUNDLE_PLAYLIST_ID, notExistId)
        if (notExistId == id) return@lazy null
        PlaylistId(id)
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

    override fun addPlaylist(playlist: Playlist, videoDetail: VideoDetail) {
        viewModel.executeAddPlaylist(playlist, videoDetail)
    }

    override fun showVideo(video: Video) {
        val videoId = video.id
        val channelId = video.channel.id
        player?.loadVideo(videoId.value)
        showDetail(videoId, channelId)
    }

    override fun showChannelDetail(channelDetail: ChannelDetail) {
        startActivity(ChannelDetailActivity.createIntent(this, channelDetail.title, channelDetail.id.value, channelDetail.thumbnailUrl.value))
    }

    private fun setupObserve() {
        viewModel.addedPlaylistLiveData.observe(this) {
            // TODO show success added playlist
        }
    }

    private fun initViews() {
        showDetail(videoId, channelId)
    }

    private fun showDetail(videoId: VideoId, channelId: ChannelId) {
        val playlistId = playlistId
        val fragment: Fragment = if (playlistId != null) {
            DetailInPlaylistFragment.newInstance(videoId, channelId, playlistId)
        } else {
            DetailInSearchFragment.newInstance(videoId, channelId)
        }
        replace(R.id.detailContainer, fragment)
    }

    private fun showChannelDetailScreen(channelName: String, channelId: String, thumbnailUrl: String, transitionView: Pair<View, String>) {
        val bundle = ActivityOptions.makeSceneTransitionAnimation(this, transitionView).toBundle()
        startActivity(ChannelDetailActivity.createIntent(this, channelName, channelId, thumbnailUrl), bundle)
    }

    companion object {

        private const val BUNDLE_VIDEO_ID = "BUNDLE_VIDEO_ID"
        private const val BUNDLE_CHANNEL_ID = "BUNDLE_CHANNEL_ID"
        private const val BUNDLE_PLAYLIST_ID = "BUNDLE_PLAYLIST_ID"

        fun createIntent(context: Context, video: Video.Search): Intent = Intent(context, VideoDetailActivity::class.java).apply {
            putExtra(BUNDLE_VIDEO_ID, video.id.value)
            putExtra(BUNDLE_CHANNEL_ID, video.channel.id.value)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        fun createIntent(context: Context, video: Video.InPlaylist): Intent = Intent(context, VideoDetailActivity::class.java).apply {
            putExtra(BUNDLE_VIDEO_ID, video.id.value)
            putExtra(BUNDLE_CHANNEL_ID, video.channel.id.value)
            putExtra(BUNDLE_PLAYLIST_ID, video.playlistId.value)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}