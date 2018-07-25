package com.numero.itube.viewmodel

import androidx.lifecycle.LiveData

interface IProgressViewModel {
    val isShowProgress: LiveData<Boolean>
}