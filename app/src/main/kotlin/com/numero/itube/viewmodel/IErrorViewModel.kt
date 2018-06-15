package com.numero.itube.viewmodel

import androidx.lifecycle.MutableLiveData

interface IErrorViewModel {
    val error: MutableLiveData<Throwable>

    val isShowError: MutableLiveData<Boolean>
}