package com.numero.itube.activity

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Pair
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.forEach
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerFragment
import com.numero.itube.R
import com.numero.itube.api.response.ChannelResponse
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.api.response.VideoDetailResponse
import com.numero.itube.extension.*
import com.numero.itube.fragment.PlayerSettingsBottomSheetFragment
import com.numero.itube.presenter.IPlayerPresenter
import com.numero.itube.presenter.PlayerPresenter
import com.numero.itube.repository.ConfigRepository
import com.numero.itube.repository.FavoriteVideoRepository
import com.numero.itube.repository.YoutubeRepository
import com.numero.itube.repository.db.FavoriteVideo
import com.numero.itube.view.adapter.FavoriteVideoAdapter
import com.numero.itube.view.adapter.RelativeVideoAdapter
import com.numero.itube.viewmodel.PlayerViewModel
import kotlinx.android.synthetic.main.activity_player.*
import kotlinx.android.synthetic.main.container_player.*
import kotlinx.android.synthetic.main.container_video_detail.*
import javax.inject.Inject

class PlayerActivity : AppCompatActivity(),
        YouTubePlayer.OnInitializedListener,
        Toolbar.OnMenuItemClickListener,
        YouTubePlayer.PlayerStateChangeListener {

    private val title: String by lazy { intent.getStringExtra(BUNDLE_TITLE) }
    private val videoId: String by lazy { intent.getStringExtra(BUNDLE_VIDEO_ID) }
    private val channelId: String by lazy { intent.getStringExtra(BUNDLE_CHANNEL_ID) }
    private val isShownFavoriteVideo: Boolean by lazy { intent.getBooleanExtra(BUNDLE_IS_FAVORITE_VIDEO, false) }
    private var player: YouTubePlayer? = null

    private val favoriteVideoAdapter: FavoriteVideoAdapter = FavoriteVideoAdapter()
    private val relativeVideoAdapter: RelativeVideoAdapter = RelativeVideoAdapter()
    private lateinit var presenter: IPlayerPresenter
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    @Inject
    lateinit var youtubeRepository: YoutubeRepository
    @Inject
    lateinit var favoriteVideoRepository: FavoriteVideoRepository
    @Inject
    lateinit var configRepository: ConfigRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component?.inject(this)
        setTheme(configRepository.theme)
        setContentView(R.layout.activity_player)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            val colorOnPrimary = getAttrColor(R.attr.colorOnPrimary)
            val drawable = getTintedDrawable(R.drawable.ic_arrow_back, colorOnPrimary) ?: return
            setHomeAsUpIndicator(drawable)

            title = this@PlayerActivity.title
        }

        initViews()
        val viewModel = initViewModel()

        presenter = PlayerPresenter(viewModel, youtubeRepository, favoriteVideoRepository, configRepository, videoId, channelId)

        val youTubePlayerFragment = YouTubePlayerFragment.newInstance().apply {
            this@PlayerActivity.fragmentManager.beginTransaction().replace(R.id.playerContainer, this).commit()
//            replace(R.id.playerContainer, this, false)
        }
        youTubePlayerFragment.initialize(configRepository.apiKey, this)

        presenter.loadVideoAndChannelDetail()
        presenter.checkFavorite()
        presenter.loadFavoriteVideo()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        item ?: return false
        when (item.itemId) {
            R.id.action_player_option -> {
                PlayerSettingsBottomSheetFragment.newInstance().show(supportFragmentManager)
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
        favoriteVideoAdapter.playNextVideo()
    }

    override fun onError(p0: YouTubePlayer.ErrorReason?) {
    }

    private fun initViewModel(): PlayerViewModel {
        val viewModel = ViewModelProviders.of(this).get(PlayerViewModel::class.java)
        viewModel.favoriteVideoList.observeNonNull(this) {
            favoriteVideoAdapter.videoList = it
        }
        viewModel.relativeVideoList.observeNonNull(this) {
            relativeVideoAdapter.videoList = it
        }
        viewModel.isShowProgress.observeNonNull(this) { isShow: Boolean ->
            // FIXME 型推論でエラーが出る
            progressBar.isInvisible = isShow.not()
            videoDetailLayout.isInvisible = isShow
        }
        viewModel.channel.observeNonNull(this) {
            showChannelDetail(it, channelId)
        }
        viewModel.videoDetail.observeNonNull(this) {
            showVideoDetail(it)
        }
        viewModel.isFavorite.observeNonNull(this) {
            registeredFavorite(it)
        }
        viewModel.isShowError.observeNonNull(this) { isShow: Boolean ->
            errorView.isInvisible = isShow.not()
        }
        return viewModel
    }

    private fun initViews() {
        bottomSheetBehavior = BottomSheetBehavior.from(favoriteListBottomSheetLayout).apply {
            state = BottomSheetBehavior.STATE_HIDDEN
        }

        bottomAppBar.apply {
            replaceMenu(R.menu.menu_player)
            val colorOnPrimary = getAttrColor(R.attr.colorOnPrimary)
            menu.forEach {
                it.setTint(colorOnPrimary)
            }

            setOnMenuItemClickListener(this@PlayerActivity)

            if (isShownFavoriteVideo) {
                val drawable = getTintedDrawable(R.drawable.ic_playlist_play, colorOnPrimary)
                navigationIcon = drawable

                setNavigationOnClickListener {
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
        }
        fab.setOnClickListener {
            presenter.changeFavorite()
        }
        favoriteVideoAdapter.apply {
            setOnItemClickListener {
                // 再生画面へ遷移
                showVideo(it)
            }
            currentVideoId = videoId
        }
        favoriteVideoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = favoriteVideoAdapter
        }
        relativeVideoAdapter.setOnItemClickListener {
            // 再生画面へ遷移
            showVideo(it)
        }
        relativeVideoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            adapter = relativeVideoAdapter
        }

        titleLayout.setOnClickListener {
            if (detailMotionLayout.progress > 0.5f) {
                detailMotionLayout.transitionToStart()
            } else {
                detailMotionLayout.transitionToEnd()
            }
        }
        errorView.setOnRetryListener {
            presenter.loadVideoAndChannelDetail()
        }
    }

    private fun showChannelDetail(channel: ChannelResponse.Channel, channelId: String) {
        channelNameTextView.text = channel.snippet.title

        val url = channel.snippet.thumbnails.medium.url
        Glide.with(this).load(url).apply(RequestOptions().circleCrop()).into(channelImageView)
        channelLayout.setOnClickListener {
            val channelName = channelNameTextView.text.toString()
            showChannelDetailScreen(channelName, channelId, url, Pair(channelImageView, channelImageView.transitionName))
        }
    }

    private fun showVideoDetail(videoDetail: VideoDetailResponse.VideoDetail) {
        titleTextView.text = videoDetail.snippet.title
        descriptionTextView.text = videoDetail.snippet.description
    }

    private fun registeredFavorite(isRegistered: Boolean) {
        fab.setImageResource(if (isRegistered) R.drawable.ic_favorite else R.drawable.ic_favorite_border)
    }

    private fun showVideo(video: SearchResponse.Video) {
        startActivity(PlayerActivity.createIntent(this, video))
        overridePendingTransition(0, 0)
    }

    private fun showVideo(video: FavoriteVideo) {
        startActivity(PlayerActivity.createIntent(this, video))
        overridePendingTransition(0, 0)
    }

    private fun showChannelDetailScreen(channelName: String, channelId: String, thumbnailUrl: String, transitionView: Pair<View, String>) {
        val bundle = ActivityOptions.makeSceneTransitionAnimation(this, transitionView).toBundle()
        startActivity(ChannelDetailActivity.createIntent(this, channelName, channelId, thumbnailUrl), bundle)
    }

    companion object {

        private const val BUNDLE_TITLE = "BUNDLE_TITLE"
        private const val BUNDLE_VIDEO_ID = "BUNDLE_VIDEO_ID"
        private const val BUNDLE_CHANNEL_ID = "BUNDLE_CHANNEL_ID"
        private const val BUNDLE_IS_FAVORITE_VIDEO = "BUNDLE_IS_FAVORITE_VIDEO"

        fun createIntent(context: Context, video: SearchResponse.Video): Intent = Intent(context, PlayerActivity::class.java).apply {
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