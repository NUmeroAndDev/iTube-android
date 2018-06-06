package com.numero.itube.view

interface IErrorHandle {
    fun showErrorMessage(e: Throwable?)

    fun hideErrorMessage()
}