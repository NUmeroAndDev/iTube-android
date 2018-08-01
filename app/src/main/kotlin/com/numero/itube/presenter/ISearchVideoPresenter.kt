package com.numero.itube.presenter

interface ISearchVideoPresenter : IPresenter {
    fun search(searchWord: String)

    fun requestMore()

    fun retry()

    fun clear()
}