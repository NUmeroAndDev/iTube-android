package com.numero.itube.fragment

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.numero.itube.R
import com.numero.itube.extension.component
import com.numero.itube.extension.observeNonNull
import com.numero.itube.presenter.FavoriteVideoListPresenter
import com.numero.itube.presenter.IFavoriteVideoListPresenter
import com.numero.itube.repository.FavoriteVideoRepository
import com.numero.itube.repository.db.FavoriteVideo
import com.numero.itube.view.adapter.FavoriteVideoAdapter
import com.numero.itube.viewmodel.FavoriteVideoListViewModel
import kotlinx.android.synthetic.main.fragment_favorite_list.view.*
import javax.inject.Inject

class FavoriteListBottomSheetFragment : BottomSheetDialogFragment() {

    @Inject
    lateinit var favoriteVideoRepository: FavoriteVideoRepository
    private lateinit var presenter: IFavoriteVideoListPresenter
    private val videoId: String by lazy { arguments?.getString(ARG_VIDEO_ID) as String }

    private val favoriteVideoAdapter: FavoriteVideoAdapter = FavoriteVideoAdapter()
    private var transition: IFavoriteListTransition? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is IFavoriteListTransition) {
            transition = context
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        component?.inject(this)
        super.onCreate(savedInstanceState)

        val viewModel = initViewModel()

        presenter = FavoriteVideoListPresenter(viewModel, favoriteVideoRepository)
    }

    override fun onResume() {
        super.onResume()
        presenter.loadFavoriteVideoList()
    }

    override fun setupDialog(dialog: Dialog, style: Int) {
        val view = View.inflate(context, R.layout.fragment_favorite_list, null)
        setupFavoriteVideoList(view)
        dialog.setContentView(view)
    }

    private fun initViewModel(): FavoriteVideoListViewModel {
        val viewModel = ViewModelProviders.of(this).get(FavoriteVideoListViewModel::class.java)
        viewModel.videoList.observeNonNull(this) {
            favoriteVideoAdapter.videoList = it
        }
        return viewModel
    }

    private fun setupFavoriteVideoList(view: View) {
        favoriteVideoAdapter.apply {
            setOnItemClickListener {
                transition?.playFavoriteVideo(it)
            }
            currentVideoId = videoId
        }
        view.favoriteVideoRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            setHasFixedSize(true)
            adapter = favoriteVideoAdapter
        }
    }

    fun show(manager: FragmentManager) {
        if (manager.findFragmentByTag(TAG) != null) {
            return
        }
        show(manager, TAG)
    }

    interface IFavoriteListTransition {
        fun playFavoriteVideo(favoriteVideo: FavoriteVideo)
    }

    companion object {
        private const val TAG = "FavoriteListBottomSheetFragment"

        private const val ARG_VIDEO_ID = "ARG_VIDEO_ID"

        fun newInstance(videoId: String): FavoriteListBottomSheetFragment = FavoriteListBottomSheetFragment().apply {
            arguments = bundleOf(ARG_VIDEO_ID to videoId)
        }
    }
}