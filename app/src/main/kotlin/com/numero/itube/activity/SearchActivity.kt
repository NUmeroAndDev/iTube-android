package com.numero.itube.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.numero.itube.R
import com.numero.itube.extension.component
import com.numero.itube.extension.hideKeyboard
import com.numero.itube.extension.observeNonNull
import com.numero.itube.repository.YoutubeRepository
import com.numero.itube.view.EndlessScrollListener
import com.numero.itube.view.adapter.VideoListAdapter
import com.numero.itube.viewmodel.SearchVideoViewModel
import com.numero.itube.viewmodel.factory.SearchVideoViewModelFactory
import kotlinx.android.synthetic.main.activity_search.*
import javax.inject.Inject

class SearchActivity : AppCompatActivity() {

    @Inject
    lateinit var youtubeRepository: YoutubeRepository

    private lateinit var viewModel: SearchVideoViewModel
    private val videoListAdapter: VideoListAdapter = VideoListAdapter()
    private var searchWord: String? = null
    private var nextPageToken: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        component?.inject(this)

        val factory = SearchVideoViewModelFactory(youtubeRepository)
        viewModel = ViewModelProviders.of(this, factory).get(SearchVideoViewModel::class.java)

        viewModel.videoList.observeNonNull(this) {
            videoListAdapter.submitList(it)
        }
        viewModel.nextPageToken.observe(this, Observer {
            nextPageToken = it
        })
        viewModel.progress.observeNonNull(this) {
            if (it) {
                progressView.show()
            } else {
                progressView.hide()
            }
        }
        viewModel.isShowError.observeNonNull(this) {
            errorGroup.visibility = if (it) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        initViews()
    }

    private fun initViews() {
        backImageButton.setOnClickListener {
            finish()
        }
        retryButton.setOnClickListener {
            val word = searchWord ?: return@setOnClickListener
            viewModel.search(getString(R.string.api_key), word)
        }
        searchEditText.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                searchWord = searchEditText.text.toString()
                val word = searchWord ?: return@setOnEditorActionListener false
                hideKeyboard()
                loadSearch(word)
            }
            return@setOnEditorActionListener false
        }
        videoListAdapter.setOnItemClickListener {
            startActivity(PlayerActivity.createIntent(this, it))
        }
        videoRecyclerView.apply {
            val manager = LinearLayoutManager(context)
            layoutManager = manager
            addOnScrollListener(EndlessScrollListener(manager) {
                if (nextPageToken == null) {
                    // トークンがない場合、追加読み込みしない
                    return@EndlessScrollListener
                }
                val word = searchWord ?: return@EndlessScrollListener
                loadSearch(word, nextPageToken)
            })
            setHasFixedSize(true)
            adapter = videoListAdapter
        }
    }

    private fun loadSearch(searchWord: String, nextPageToken: String? = null) {
        viewModel.search(getString(R.string.api_key), searchWord, nextPageToken)
    }

    private fun clearSearching() {
        searchWord = null
        searchEditText.setText("")
        videoListAdapter.clearList()
        errorGroup.visibility = View.GONE
    }

    companion object {
        fun createIntent(context: Context) = Intent(context, SearchActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        }
    }
}
