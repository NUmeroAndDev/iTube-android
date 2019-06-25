package com.numero.itube.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.numero.itube.model.Action
import com.numero.itube.model.EmptyAction
import com.numero.itube.model.PlaylistList
import com.numero.itube.repository.PlaylistRepository
import javax.inject.Inject

class PlaylistViewModel @Inject constructor(
        private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val actionLiveData: MutableLiveData<Action<EmptyAction>> = MutableLiveData()
    val playlistListLiveData: LiveData<PlaylistList> = actionLiveData.switchMap {
        playlistRepository.readPlaylistList()
    }

    fun executeLoadPlaylist() {
        actionLiveData.value = Action(EmptyAction)
    }
}