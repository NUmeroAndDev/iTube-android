package com.numero.itube.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.numero.itube.R
import com.numero.itube.extension.component
import com.numero.itube.extension.observeNonNull
import com.numero.itube.presenter.ISearchVideoPresenter
import com.numero.itube.presenter.SearchVideoPresenter
import com.numero.itube.repository.ConfigRepository
import com.numero.itube.repository.YoutubeRepository
import com.numero.itube.view.EndlessScrollListener
import com.numero.itube.view.SearchInputView
import com.numero.itube.view.adapter.VideoListAdapter
import com.numero.itube.viewmodel.SearchVideoViewModel
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    @Inject
    lateinit var youtubeRepository: YoutubeRepository
    @Inject
    lateinit var configRepository: ConfigRepository

    private lateinit var presenter: ISearchVideoPresenter
    private val videoListAdapter: VideoListAdapter = VideoListAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)
        component?.inject(this)

        supportActionBar?.apply {
            setHomeAsUpIndicator(R.drawable.ic_arrow_back)
            setDisplayHomeAsUpEnabled(true)
        }

        val viewModel = ViewModelProviders.of(this).get(SearchVideoViewModel::class.java)
        viewModel.videoList.observeNonNull(this) {
            videoListAdapter.submitList(it)
        }
        viewModel.isShowProgress.observeNonNull(this) { isShow: Boolean ->
            progressBar.isInvisible = isShow.not()
        }
        viewModel.isShowError.observeNonNull(this) { isShow: Boolean ->
            errorView.isInvisible = isShow.not()
        }

        presenter = SearchVideoPresenter(viewModel, youtubeRepository, configRepository)

        initViews()
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

    private fun initViews() {
        errorView.setOnRetryListener {
            presenter.retry()
        }
        searchInputEditText.setOnQueryTextListener(object : SearchInputView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String) {
                presenter.search(query)
            }

            override fun onQueryTextChange(newText: String) {
                // 初期化
                presenter.clear()
            }
        })
        videoListAdapter.setOnItemClickListener {
            startActivity(PlayerActivity.createIntent(this, it))
        }
        videoRecyclerView.apply {
            val manager = LinearLayoutManager(context)
            layoutManager = manager
            addOnScrollListener(EndlessScrollListener(manager) {
                presenter.requestMore()
            })
            setHasFixedSize(true)
            adapter = videoListAdapter
        }
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, SearchActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}
