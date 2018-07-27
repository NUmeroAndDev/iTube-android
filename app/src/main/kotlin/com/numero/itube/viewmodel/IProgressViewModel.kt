package com.numero.itube.viewmodel

import androidx.lifecycle.LiveData

interface IProgressViewModel {
    val isShowProgress: LiveData<Boolean>

    val isProcessing: Boolean
        get() {
            val isShownProgress = isShowProgress.value
            return isShownProgress != null && isShownProgress
        }
}