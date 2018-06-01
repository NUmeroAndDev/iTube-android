package com.numero.itube.activity

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.numero.itube.R
import com.numero.itube.extension.component
import com.numero.itube.extension.findFragment
import com.numero.itube.extension.replace
import com.numero.itube.fragment.DetailFragment
import com.numero.itube.fragment.PlayerSettingsBottomSheetFragment
import com.numero.itube.fragment.RelativeFavoriteFragment
import com.numero.itube.fragment.RelativeFragment
import com.numero.itube.model.Video
import com.numero.itube.repository.ConfigRepository
import com.numero.itube.repository.db.FavoriteVideo
import kotlinx.android.synthetic.main.activity_player.*
import javax.inject.Inject

class PlayerActivity : AppCompatActivity(),
        YouTubePlayer.OnInitializedListener,
        RelativeFragment.RelativeFragmentListener,
        RelativeFavoriteFragment.RelativeFavoriteFragmentListener,
        DetailFragment.DetailFragmentListener,
        Toolbar.OnMenuItemClickListener,
        YouTubePlayer.PlayerStateChangeListener {

    private val title: String by lazy { intent.getStringExtra(BUNDLE_TITLE) }
    private val videoId: String by lazy { intent.getStringExtra(BUNDLE_VIDEO_ID) }
    private val channelId: String by lazy { intent.getStringExtra(BUNDLE_CHANNEL_ID) }
    private val isFavoriteVideo: Boolean by lazy { intent.getBooleanExtra(BUNDLE_IS_FAVORITE_VIDEO, false) }
    private var player: YouTubePlayer? = null
    private var isRegistered: Boolean = false

    @Inject
    lateinit var configRepository: ConfigRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component?.inject(this)
        setContentView(R.layout.activity_player)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = this@PlayerActivity.title
        }

        val youTubePlayerFragment = YouTubePlayerFragment.newInstance().apply {
            this@PlayerActivity.fragmentManager.beginTransaction().replace(R.id.playerContainer, this).commit()
//            replace(R.id.playerContainer, this, false)
        }
        youTubePlayerFragment.initialize(getString(R.string.api_key), this)

        if (findFragment(R.id.relativeContainer) == null) {
            val fragment: Fragment = if (isFavoriteVideo) {
                RelativeFavoriteFragment.newInstance(videoId, channelId)
            } else {
                RelativeFragment.newInstance(videoId, channelId)
            }
            replace(R.id.relativeContainer, fragment, false)
        }

        bottomAppBar.apply {
            replaceMenu(R.menu.menu_player)
            setOnMenuItemClickListener(this@PlayerActivity)
        }

        fab.setOnClickListener {
            isRegistered = isRegistered.not()
            val fragment = findFragment(R.id.detailContainer)
            if (fragment is DetailFragment) {
                fragment.setIsRegistered(isRegistered)
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        item ?: return false
        when (item.itemId) {
            R.id.action_settings -> {
                PlayerSettingsBottomSheetFragment.newInstance().show(supportFragmentManager, PlayerSettingsBottomSheetFragment.TAG)
            }
        }
        return true
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
            setPlayerStateChangeListener(this@PlayerActivity)
        }
        if (b.not()) {
            player?.loadVideo(videoId)
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
        val fragment = findFragment(R.id.relativeContainer)
        if (fragment is RelativeFavoriteFragment) {
            fragment.playNextVideo()
        }
    }

    override fun onError(p0: YouTubePlayer.ErrorReason?) {
    }

    override fun showVideo(video: Video) {
        startActivity(PlayerActivity.createIntent(this, video))
        overridePendingTransition(0, 0)
    }

    override fun showVideo(video: FavoriteVideo) {
        startActivity(PlayerActivity.createIntent(this, video))
        overridePendingTransition(0, 0)
    }

    override fun onIsRegisteredFavorite(isRegisteredFavorite: Boolean) {
        isRegistered = isRegisteredFavorite
        fab.setImageResource(if (isRegisteredFavorite) R.drawable.ic_favorite else R.drawable.ic_favorite_border)
    }

    override fun onClickChannel(channelName: String, channelId: String, thumbnailUrl: String, vararg transitionViews: Pair<View, String>) {
        val bundle = ActivityOptions.makeSceneTransitionAnimation(this, *transitionViews).toBundle()
        startActivity(ChannelDetailActivity.createIntent(this, channelName, channelId, thumbnailUrl), bundle)
    }

    companion object {

        private const val BUNDLE_TITLE = "BUNDLE_TITLE"
        private const val BUNDLE_VIDEO_ID = "BUNDLE_VIDEO_ID"
        private const val BUNDLE_CHANNEL_ID = "BUNDLE_CHANNEL_ID"
        private const val BUNDLE_IS_FAVORITE_VIDEO = "BUNDLE_IS_FAVORITE_VIDEO"

        fun createIntent(context: Context, video: Video): Intent = Intent(context, PlayerActivity::class.java).apply {
            putExtra(BUNDLE_TITLE, video.snippet.title)
            putExtra(BUNDLE_VIDEO_ID, video.id.videoId)
            putExtra(BUNDLE_CHANNEL_ID, video.snippet.channelId)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        fun createIntent(context: Context, favoriteVideo: FavoriteVideo): Intent = Intent(context, PlayerActivity::class.java).apply {
            putExtra(BUNDLE_TITLE, favoriteVideo.title)
            putExtra(BUNDLE_VIDEO_ID, favoriteVideo.id)
            putExtra(BUNDLE_CHANNEL_ID, favoriteVideo.channelId)
            putExtra(BUNDLE_IS_FAVORITE_VIDEO, true)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}