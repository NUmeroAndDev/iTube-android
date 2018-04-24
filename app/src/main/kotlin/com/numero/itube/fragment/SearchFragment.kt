package com.numero.itube.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.numero.itube.R
import com.numero.itube.contract.SearchContract
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

        searchView.apply {
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query == null || query.isEmpty()) {
                        return false
                    }
                    presenter.search(getString(R.string.api_key), query)
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean = false
            })
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

    companion object {
        fun newInstance(): SearchFragment = SearchFragment()
    }
}