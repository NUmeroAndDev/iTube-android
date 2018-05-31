package com.numero.itube.contract

import com.numero.itube.presenter.IPresenter
import com.numero.itube.view.IView

interface ChannelDetailContract {

    interface View : IView<Presenter> {
        fun showBannerImage(imageUrl: String)

        fun showErrorMessage(e: Throwable?)

        fun showProgress()

        fun dismissProgress()
    }

    interface Presenter : IPresenter {
        fun loadChannelDetail(key: String)
    }
}