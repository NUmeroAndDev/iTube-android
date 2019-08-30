package com.numero.itube.ui.top

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.numero.itube.R
import com.numero.itube.extension.component
import com.numero.itube.model.PlaylistSummaryList
import com.numero.itube.repository.ConfigRepository
import com.numero.itube.ui.playlist.InputPlaylistTitleDialogFragment
import com.numero.itube.ui.playlist.PlaylistListActivity
import com.numero.itube.ui.search.SearchActivity
import com.numero.itube.ui.top.item.PlaylistSummaryItem
import com.numero.itube.ui.video.detail.VideoDetailActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_top.*
import javax.inject.Inject

class TopActivity : AppCompatActivity(),
        InputPlaylistTitleDialogFragment.InputPlaylistTitleCallback {

    @Inject
    lateinit var configRepository: ConfigRepository
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val groupieAdapter = GroupAdapter<ViewHolder>()

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(TopViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component?.inject(this)
        setTheme(configRepository.theme)
        setContentView(R.layout.activity_top)

        viewModel.playlistSummaryListLiveData.observe(this) {
            groupieAdapter.clear()
            groupieAdapter.add(it.toSection())
        }
        viewModel.createPlaylistListLiveData.observe(this) {
            // TODO show success message
            viewModel.executeLoadAllPlaylist()
        }

        groupieAdapter.setOnItemClickListener { item, _ ->
            if (item is PlaylistSummaryItem) {
                val video = item.playlistSummary.video ?: return@setOnItemClickListener
                startActivity(VideoDetailActivity.createIntent(this, video))
            }
        }
        videoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = groupieAdapter
        }
        searchCardView.setOnClickListener {
            startActivity(SearchActivity.createIntent(this@TopActivity))
        }
        addPlayListFloatingActionButton.setOnClickListener {
            showPlaylistTitleInputDialog()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(PlaylistListActivity.createIntent(this))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.executeLoadAllPlaylist()
    }

    override fun createPlaylist(title: String) {
        viewModel.executeCreatePlaylist(title)
    }

    private fun showPlaylistTitleInputDialog() {
        InputPlaylistTitleDialogFragment.newInstance().show(supportFragmentManager)
    }

    private fun PlaylistSummaryList.toSection(): Section {
        return Section().apply {
            addAll(value.map { PlaylistSummaryItem(it) })
        }
    }
}