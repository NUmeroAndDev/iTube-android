package com.numero.itube.contract

import com.numero.itube.presenter.IPresenter

class SearchVideoContract {

    interface Presenter : IPresenter {
        fun search(key: String, searchWord: String)

        fun requestMore(key: String)

        fun retry(key: String)

        fun clear()
    }
}