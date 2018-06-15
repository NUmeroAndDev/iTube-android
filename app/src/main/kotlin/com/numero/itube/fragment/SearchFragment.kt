package com.numero.itube.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.numero.itube.R
import com.numero.itube.api.response.SearchResponse
import com.numero.itube.extension.component
import com.numero.itube.extension.hideKeyboard
import com.numero.itube.extension.observeNonNull
import com.numero.itube.repository.YoutubeRepository
import com.numero.itube.view.EndlessScrollListener
import com.numero.itube.view.adapter.VideoListAdapter
import com.numero.itube.viewmodel.SearchVideoViewModel
import com.numero.itube.viewmodel.factory.SearchVideoViewModelFactory
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchFragment : Fragment() {

    @Inject
    lateinit var youtubeRepository: YoutubeRepository

    private lateinit var viewModel: SearchVideoViewModel
    private val videoListAdapter: VideoListAdapter = VideoListAdapter()
    private var listener: SearchFragmentListener? = null
    private var searchWord: String? = null
    private var nextPageToken: String? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SearchFragmentListener) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        retryButton.setOnClickListener {
            val word = searchWord ?: return@setOnClickListener
            viewModel.search(getString(R.string.api_key), word)
        }

        searchEditText.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                searchWord = searchEditText.text.toString()
                val word = searchWord ?: return@setOnEditorActionListener false
                hideKeyboard()
                viewModel.search(getString(R.string.api_key), word)
            }
            return@setOnEditorActionListener false
        }
        videoListAdapter.setOnItemClickListener {
            // 再生画面へ遷移
            listener?.showVideo(it)
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
                viewModel.search(getString(R.string.api_key), word, nextPageToken)
            })
            setHasFixedSize(true)
            adapter = videoListAdapter
        }
    }

    fun clearSearching() {
        searchWord = null
        searchEditText.setText("")
        videoListAdapter.clearList()
        errorGroup.visibility = View.GONE
    }

    interface SearchFragmentListener {
        fun showVideo(video: SearchResponse.Video)
    }

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }
}