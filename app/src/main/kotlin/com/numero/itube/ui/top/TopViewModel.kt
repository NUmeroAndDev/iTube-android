package com.numero.itube.ui.top

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.numero.itube.model.Action
import com.numero.itube.model.EmptyAction
import com.numero.itube.model.PlaylistDetailList
import com.numero.itube.repository.PlaylistRepository
import javax.inject.Inject

class TopViewModel @Inject constructor(
        private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val actionLiveData: MutableLiveData<Action<EmptyAction>> = MutableLiveData()
    val playlistDetailListLiveData: LiveData<PlaylistDetailList> = actionLiveData.switchMap {
        playlistRepository.readPlaylistDetailList()
    }

    fun executeLoadAllPlaylist() {
        actionLiveData.value = Action(EmptyAction)
    }
}