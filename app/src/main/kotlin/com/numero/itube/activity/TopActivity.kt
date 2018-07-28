package com.numero.itube.activity

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.numero.itube.R
import com.numero.itube.contract.FavoriteVideoListContract
import com.numero.itube.extension.component
import com.numero.itube.extension.observeNonNull
import com.numero.itube.presenter.FavoriteVideoListPresenter
import com.numero.itube.repository.FavoriteVideoRepository
import com.numero.itube.view.adapter.FavoriteVideoListAdapter
import com.numero.itube.viewmodel.FavoriteVideoListViewModel
import kotlinx.android.synthetic.main.activity_top.*
import javax.inject.Inject

class TopActivity : AppCompatActivity() {

    @Inject
    lateinit var favoriteVideoRepository: FavoriteVideoRepository

    private lateinit var presenter: FavoriteVideoListContract.Presenter
    private val videoListAdapter: FavoriteVideoListAdapter = FavoriteVideoListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component?.inject(this)
        setContentView(R.layout.activity_top)
        setSupportActionBar(toolbar)

        val viewModel = ViewModelProviders.of(this).get(FavoriteVideoListViewModel::class.java)
        viewModel.videoList.observeNonNull(this) {
            videoListAdapter.videoList = it
        }
        viewModel.isShowEmptyMessage.observeNonNull(this) {
            noFavoriteVideoTextView.isInvisible = it.not()
        }

        presenter = FavoriteVideoListPresenter(viewModel, favoriteVideoRepository)

        videoListAdapter.setOnItemClickListener {
            startActivity(PlayerActivity.createIntent(this, it))
        }
        videoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = videoListAdapter
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(SettingsActivity.createIntent(this))
                true
            }
            R.id.action_search -> {
                startActivity(SearchActivity.createIntent(this))
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.loadFavoriteVideoList()
    }
}