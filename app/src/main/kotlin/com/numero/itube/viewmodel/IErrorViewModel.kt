package com.numero.itube.viewmodel

import androidx.lifecycle.LiveData

interface IErrorViewModel {
    val error: LiveData<Throwable>

    val isShowError: LiveData<Boolean>
}