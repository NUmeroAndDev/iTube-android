package com.numero.itube.ui.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isInvisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import com.numero.itube.R
import com.numero.itube.extension.component
import com.numero.itube.extension.getAttrColor
import com.numero.itube.extension.getTintedDrawable
import com.numero.itube.model.SearchVideoList
import com.numero.itube.repository.ConfigRepository
import com.numero.itube.ui.search.item.SearchVideoItem
import com.numero.itube.ui.video.detail.VideoDetailActivity
import com.numero.itube.view.EndlessScrollListener
import com.numero.itube.view.SearchInputView
import com.numero.itube.viewmodel.SearchVideoViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    @Inject
    lateinit var configRepository: ConfigRepository
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val groupieAdapter = GroupAdapter<ViewHolder>()

    private val viewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory).get(SearchVideoViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component?.inject(this)
        setTheme(configRepository.theme)
        setContentView(R.layout.activity_search)
        setSupportActionBar(toolbar)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            val colorOnPrimary = getAttrColor(R.attr.colorOnPrimary)
            val drawable = getTintedDrawable(R.drawable.ic_arrow_back, colorOnPrimary) ?: return
            setHomeAsUpIndicator(drawable)
        }

        initViews()
        setupObserve()
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
            viewModel.retry()
        }
        searchInputEditText.setOnQueryTextListener(object : SearchInputView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String) {
                viewModel.executeSearch(query)
            }

            override fun onQueryTextChange(newText: String) {
                // 初期化
                //viewModel.clear()
            }
        })
        groupieAdapter.setOnItemClickListener { item, _ ->
            if (item is SearchVideoItem) {
                startActivity(VideoDetailActivity.createIntent(this, item.video))
            }
        }
        videoRecyclerView.apply {
            val manager = LinearLayoutManager(context)
            layoutManager = manager
            addOnScrollListener(EndlessScrollListener(manager) {
                viewModel.executeMoreLoad()
            })
            setHasFixedSize(true)
            adapter = groupieAdapter
        }
    }

    private fun setupObserve() {
        viewModel.searchVideoListLiveData.observe(this) {
            groupieAdapter.clear()
            groupieAdapter.add(it.toSection())
        }
        viewModel.progressLiveData.observe(this) {
            progressBar.isInvisible = it.not()
        }
        viewModel.errorLiveData.observe(this) {
            errorView.isInvisible = it.not()
        }
    }

    private fun SearchVideoList.toSection(): Section {
        return Section().apply {
            addAll(videoList.map { SearchVideoItem(it) })
        }
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, SearchActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}
