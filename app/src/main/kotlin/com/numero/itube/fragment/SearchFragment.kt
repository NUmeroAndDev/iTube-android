package com.numero.itube.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.content.systemService
import com.numero.itube.R
import com.numero.itube.contract.SearchContract
import com.numero.itube.extension.hideKeyboard
import com.numero.itube.model.Video
import com.numero.itube.presenter.SearchPresenter
import com.numero.itube.repository.YoutubeRepository
import com.numero.itube.view.adapter.VideoListAdapter
import dagger.android.support.AndroidSupportInjection
import kotlinx.android.synthetic.main.fragment_search.*
import javax.inject.Inject

class SearchFragment : Fragment(), SearchContract.View {

    @Inject
    lateinit var youtubeRepository: YoutubeRepository

    private lateinit var presenter: SearchContract.Presenter
    private val videoListAdapter: VideoListAdapter = VideoListAdapter()
    private var listener: SearchFragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is SearchFragmentListener) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AndroidSupportInjection.inject(this)

        SearchPresenter(this, youtubeRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        searchEditText.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_SEARCH) {
                val query = searchEditText.text.toString()
                hideKeyboard()
                presenter.search(getString(R.string.api_key), query)
            }
            return@setOnEditorActionListener false
        }
        videoListAdapter.setOnItemClickListener {
            // 再生画面へ遷移
            listener?.showVideo(it)
        }
        videoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = videoListAdapter
        }
    }

    override fun showVideoList(videoList: List<Video>) {
        videoListAdapter.videoList = videoList
    }

    override fun clearVideoList() {
        videoListAdapter.videoList = listOf()
    }

    override fun showEmptyMessage() {

    }

    override fun showErrorMessage(e: Throwable?) {
        e?.printStackTrace()
    }

    override fun showProgress() {

    }

    override fun dismissProgress() {

    }

    override fun setPresenter(presenter: SearchContract.Presenter) {
        this.presenter = presenter
    }

    interface SearchFragmentListener {
        fun showVideo(video: Video)
    }

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }
}