package com.numero.itube.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.numero.itube.R
import com.numero.itube.contract.FavoriteVideoListContract
import com.numero.itube.extension.component
import com.numero.itube.presenter.FavoriteVideoListPresenter
import com.numero.itube.repository.FavoriteVideoRepository
import com.numero.itube.repository.db.FavoriteVideo
import com.numero.itube.view.adapter.FavoriteVideoListAdapter
import kotlinx.android.synthetic.main.fragment_favorite_video_list.*
import javax.inject.Inject

class FavoriteVideoListFragment : Fragment(), FavoriteVideoListContract.View {

    @Inject
    lateinit var favoriteVideoRepository: FavoriteVideoRepository

    private lateinit var presenter: FavoriteVideoListContract.Presenter
    private val videoListAdapter: FavoriteVideoListAdapter = FavoriteVideoListAdapter()
    private var listener: FavoriteFragmentListener? = null

    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is FavoriteFragmentListener) {
            listener = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        component?.inject(this)

        FavoriteVideoListPresenter(this, favoriteVideoRepository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_favorite_video_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        videoListAdapter.setOnItemClickListener {
            listener?.showVideo(it)
        }
        videoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            adapter = videoListAdapter
        }
    }

    override fun onResume() {
        super.onResume()
        presenter.subscribe()
    }

    override fun onPause() {
        super.onPause()
        presenter.unSubscribe()
    }

    override fun showVideoList(videoList: List<FavoriteVideo>) {
        videoListAdapter.videoList = videoList
    }

    override fun showEmptyMessage() {
        noFavoriteVideoTextView.visibility = View.VISIBLE
    }

    override fun hideEmptyMessage() {
        noFavoriteVideoTextView.visibility = View.INVISIBLE
    }

    override fun showErrorMessage(e: Throwable?) {
        e?.printStackTrace()
    }

    override fun hideErrorMessage() {
    }

    override fun showProgress() {

    }

    override fun dismissProgress() {

    }

    override fun setPresenter(presenter: FavoriteVideoListContract.Presenter) {
        this.presenter = presenter
    }

    interface FavoriteFragmentListener {
        fun showVideo(video: FavoriteVideo)
    }

    companion object {
        fun newInstance(): FavoriteVideoListFragment = FavoriteVideoListFragment()
    }
}