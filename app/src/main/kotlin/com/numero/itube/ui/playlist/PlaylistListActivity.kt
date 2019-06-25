package com.numero.itube.ui.playlist

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.numero.itube.R
import com.numero.itube.extension.component
import com.numero.itube.extension.getAttrColor
import com.numero.itube.extension.getTintedDrawable
import com.numero.itube.model.PlaylistList
import com.numero.itube.ui.playlist.item.PlaylistItem
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_playlist.*
import javax.inject.Inject

class PlaylistListActivity : AppCompatActivity() {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val groupieAdapter = GroupAdapter<ViewHolder>()

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(PlaylistViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component?.inject(this)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            val colorOnPrimary = getAttrColor(R.attr.colorOnPrimary)
            val drawable = getTintedDrawable(R.drawable.ic_arrow_back, colorOnPrimary) ?: return
            setHomeAsUpIndicator(drawable)
        }

        setupViews()
        setupObserve()
    }

    override fun onResume() {
        super.onResume()
        viewModel.executeLoadPlaylist()
    }

    private fun setupViews() {
        playlistRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@PlaylistListActivity)
            setHasFixedSize(true)
            adapter = groupieAdapter
        }
    }

    private fun setupObserve() {
        viewModel.playlistListLiveData.observe(this) {
            groupieAdapter.clear()
            groupieAdapter.add(it.toSection())
        }
    }

    private fun PlaylistList.toSection(): Section {
        return Section().apply {
            addAll(value.map { PlaylistItem(it) })
        }
    }

    companion object {
        fun createIntent(context: Context): Intent = Intent(context, PlaylistListActivity::class.java)
    }
}