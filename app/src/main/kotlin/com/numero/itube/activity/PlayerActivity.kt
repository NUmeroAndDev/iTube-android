package com.numero.itube.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerSupportFragment
import com.numero.itube.R
import com.numero.itube.extension.findFragment
import com.numero.itube.extension.replace
import com.numero.itube.fragment.DetailFragment
import com.numero.itube.fragment.RelativeFavoriteFragment
import com.numero.itube.fragment.RelativeFragment
import com.numero.itube.model.Video
import com.numero.itube.repository.db.FavoriteVideo
import kotlinx.android.synthetic.main.activity_player.*

class PlayerActivity : AppCompatActivity(),
        YouTubePlayer.OnInitializedListener,
        RelativeFragment.RelativeFragmentListener,
        RelativeFavoriteFragment.RelativeFavoriteFragmentListener,
        DetailFragment.DetailFragmentListener {

    private val title: String by lazy { intent.getStringExtra(BUNDLE_TITLE) }
    private val videoId: String by lazy { intent.getStringExtra(BUNDLE_VIDEO_ID) }
    private val channelId: String by lazy { intent.getStringExtra(BUNDLE_CHANNEL_ID) }
    private val isFavoriteVideo: Boolean by lazy { intent.getBooleanExtra(BUNDLE_IS_FAVORITE_VIDEO, false) }
    private var player: YouTubePlayer? = null
    private var isRegistered: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_player)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = this@PlayerActivity.title
        }

        val youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance().apply {
            replace(R.id.playerContainer, this, false)
        }
        youTubePlayerFragment.initialize(getString(R.string.api_key), this)

        if (findFragment(R.id.detailContainer) == null) {
            replace(R.id.detailContainer, DetailFragment.newInstance(videoId, channelId), false)
        }

        if (findFragment(R.id.relativeContainer) == null) {
            val fragment: Fragment = if (isFavoriteVideo) {
                RelativeFavoriteFragment.newInstance(videoId)
            } else {
                RelativeFragment.newInstance(videoId)
            }
            replace(R.id.relativeContainer, fragment, false)
        }

        bottomAppBar.replaceMenu(R.menu.navigation)
        fab.setOnClickListener {
            isRegistered = isRegistered.not()
            val fragment = findFragment(R.id.detailContainer)
            if (fragment is DetailFragment) {
                fragment.setIsRegistered(isRegistered)
            }
        }
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
        if (b.not()) {
            player = youTubePlayer?.apply {
                loadVideo(videoId)
            }
        }
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
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