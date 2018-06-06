package com.numero.itube.view

interface IErrorHandle {
    fun showErrorMessage(e: Throwable?, retryListener: (() -> Unit)? = null)

    fun hideErrorMessage()
}