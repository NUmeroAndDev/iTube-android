package com.numero.itube.view

import android.support.v7.widget.RecyclerView
import android.support.v7.widget.LinearLayoutManager

class EndlessScrollListener(
        private val mLinearLayoutManager: LinearLayoutManager,
        private val onLoadMoreListener: () -> Unit
) : RecyclerView.OnScrollListener() {

    private var previousTotal = 0
    private var loading = true

    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)

        val visibleItemCount = recyclerView.childCount
        val totalItemCount = mLinearLayoutManager.itemCount
        val firstVisibleItem = mLinearLayoutManager.findFirstVisibleItemPosition()

        if (loading.not() and (totalItemCount - visibleItemCount <= firstVisibleItem + VISIBLE_THRES_HOLD)) {
            onLoadMoreListener.invoke()
            loading = true
        }

        if (loading and (totalItemCount > previousTotal)) {
            loading = false
            previousTotal = totalItemCount
        }
    }

    companion object {
        private const val VISIBLE_THRES_HOLD = 2
    }
}