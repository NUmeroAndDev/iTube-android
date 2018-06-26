package com.numero.itube.viewmodel

import androidx.lifecycle.LiveData

interface IProgressViewModel {
    val progress: LiveData<Boolean>
}