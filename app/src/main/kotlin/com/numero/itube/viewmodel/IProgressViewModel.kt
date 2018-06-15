package com.numero.itube.viewmodel

import androidx.lifecycle.MutableLiveData

interface IProgressViewModel {
    val progress: MutableLiveData<Boolean>
}