package com.numero.itube.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.numero.itube.R
import com.numero.itube.contract.FavoriteVideoListContract
import com.numero.itube.extension.component
import com.numero.itube.extension.observeNonNull
import com.numero.itube.presenter.FavoriteVideoListPresenter
import com.numero.itube.repository.FavoriteVideoRepository
import com.numero.itube.repository.db.FavoriteVideo
import com.numero.itube.view.adapter.FavoriteVideoListAdapter
import com.numero.itube.viewmodel.FavoriteVideoListViewModel
import kotlinx.android.synthetic.main.fragment_favorite_video_list.*
import javax.inject.Inject

class FavoriteVideoListFragment : Fragment() {

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

        val viewModel = ViewModelProviders.of(this).get(FavoriteVideoListViewModel::class.java)
        viewModel.videoList.observeNonNull(this) {
            videoListAdapter.videoList = it
        }
        viewModel.isShowEmptyMessage.observeNonNull(this) {
            noFavoriteVideoTextView.isInvisible = it.not()
        }

        presenter = FavoriteVideoListPresenter(viewModel, favoriteVideoRepository)
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
        presenter.loadFavoriteVideoList()
    }

    interface FavoriteFragmentListener {
        fun showVideo(video: FavoriteVideo)
    }

    companion object {
        fun newInstance(): FavoriteVideoListFragment = FavoriteVideoListFragment()
    }
}