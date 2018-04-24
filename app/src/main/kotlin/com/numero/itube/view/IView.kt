package com.numero.itube.view

interface IView<in T> {

    fun setPresenter(presenter: T)

}