package com.numero.itube.contract

import com.numero.itube.presenter.IPresenter

class SearchVideoContract {

    interface Presenter : IPresenter {
        fun search(searchWord: String)

        fun requestMore()

        fun retry()

        fun clear()
    }
}