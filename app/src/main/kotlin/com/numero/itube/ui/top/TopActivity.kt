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
import com.numero.itube.model.PlaylistDetailList
import com.numero.itube.repository.ConfigRepository
import com.numero.itube.ui.playlist.PlaylistListActivity
import com.numero.itube.ui.search.SearchActivity
import com.numero.itube.ui.top.item.PlaylistHeaderItem
import com.numero.itube.ui.top.item.PlaylistVideoItem
import com.numero.itube.ui.video.VideoDetailActivity
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_top.*
import javax.inject.Inject

class TopActivity : AppCompatActivity() {

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
        setSupportActionBar(toolbar)


        viewModel.playlistDetailListLiveData.observe(this) {
            groupieAdapter.clear()
            groupieAdapter.addAll(it.toSectionList())
        }

        groupieAdapter.setOnItemClickListener { item, _ ->
            if (item is PlaylistVideoItem) {
                startActivity(VideoDetailActivity.createIntent(this, item.video))
            }
        }
        videoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = groupieAdapter
        }
        addButton.setOnClickListener {
            startActivity(SearchActivity.createIntent(this@TopActivity))
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

    private fun PlaylistDetailList.toSectionList(): List<Section> {
        return value.map { playlist ->
            Section().apply {
                setHeader(PlaylistHeaderItem(playlist))
                addAll(playlist.videoList.map { PlaylistVideoItem(it) })
            }
        }
    }
}