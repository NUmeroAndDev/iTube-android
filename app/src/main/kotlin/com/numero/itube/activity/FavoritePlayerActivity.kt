package com.numero.itube.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
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
import com.numero.itube.repository.db.FavoriteVideo
import kotlinx.android.synthetic.main.activity_favorite_player.*

class FavoritePlayerActivity : AppCompatActivity(),
        YouTubePlayer.OnInitializedListener,
        RelativeFavoriteFragment.RelativeFavoriteFragmentListener {

    private val favoriteVideo: FavoriteVideo by lazy { intent.getSerializableExtra(BUNDLE_FAVORITE_VIDEO) as FavoriteVideo }
    private var player: YouTubePlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite_player)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = favoriteVideo.title
        }

        val youTubePlayerFragment = YouTubePlayerSupportFragment.newInstance().apply {
            replace(R.id.playerContainer, this, false)
        }
        youTubePlayerFragment.initialize(getString(R.string.api_key), this)

        if (findFragment(R.id.detailContainer) == null) {
            replace(R.id.detailContainer, DetailFragment.newInstance(favoriteVideo.id, favoriteVideo.channelId), false)
        }
        if (findFragment(R.id.favoriteListContainer) == null) {
            replace(R.id.favoriteListContainer, RelativeFavoriteFragment.newInstance(favoriteVideo.id), false)
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
                loadVideo(favoriteVideo.id)
            }
        }
    }

    override fun onInitializationFailure(p0: YouTubePlayer.Provider?, p1: YouTubeInitializationResult?) {
    }

    override fun showVideo(video: FavoriteVideo) {
        startActivity(FavoritePlayerActivity.createIntent(this, video))
        overridePendingTransition(0, 0)
    }

    companion object {
        private const val BUNDLE_FAVORITE_VIDEO = "BUNDLE_FAVORITE_VIDEO"

        fun createIntent(context: Context, favoriteVideo: FavoriteVideo): Intent = Intent(context, FavoritePlayerActivity::class.java).apply {
            putExtra(BUNDLE_FAVORITE_VIDEO, favoriteVideo)
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}