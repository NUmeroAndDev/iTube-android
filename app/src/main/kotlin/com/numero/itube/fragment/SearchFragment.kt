package com.numero.itube.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.numero.itube.R
import com.numero.itube.contract.SearchContract
import com.numero.itube.extension.component
import com.numero.itube.extension.hideKeyboard
import com.numero.itube.model.Video
import com.numero.itube.presenter.SearchPresenter
import com.numero.itube.repository.YoutubeRepository
import com.numero.itube.view.EndlessScrollListener
import com.numero.itube.view.adapter.VideoListAdapter
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchFragment : Fragment(), SearchContract.View {

    @Inject
    lateinit var youtubeRepository: YoutubeRepository

    private lateinit var presenter: SearchContract.Presenter
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

        SearchPresenter(this, youtubeRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        searchEditText.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                searchWord = searchEditText.text.toString()
                val word = searchWord ?: return@setOnEditorActionListener false
                hideKeyboard()
                presenter.search(getString(R.string.api_key), word)
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
                presenter.search(getString(R.string.api_key), word, nextPageToken)
            })
            setHasFixedSize(true)
            adapter = videoListAdapter
        }
    }

    override fun showVideoList(videoList: List<Video>, nextPageToken: String?) {
        this.nextPageToken = nextPageToken
        videoListAdapter.videoList = videoList.toMutableList()
    }

    override fun addVideoList(videoList: List<Video>, nextPageToken: String?) {
        this.nextPageToken = nextPageToken
        videoListAdapter.addVideoList(videoList)
    }

    override fun clearVideoList() {
        videoListAdapter.videoList = mutableListOf()
    }

    override fun showEmptyMessage() {

    }

    override fun showErrorMessage(e: Throwable?) {
        e?.printStackTrace()
    }

    override fun showProgress() {
        progressView?.show()
    }

    override fun dismissProgress() {
        progressView?.hide()
    }

    override fun setPresenter(presenter: SearchContract.Presenter) {
        this.presenter = presenter
    }

    fun clearSearching() {
        searchWord = null
        searchEditText.setText("")
        videoListAdapter.videoList = mutableListOf()
    }

    interface SearchFragmentListener {
        fun showVideo(video: Video)
    }

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }
}