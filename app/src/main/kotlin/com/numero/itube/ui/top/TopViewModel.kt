package com.numero.itube.ui.top

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.numero.itube.model.Action
import com.numero.itube.model.EmptyAction
import com.numero.itube.model.Playlist
import com.numero.itube.model.PlaylistSummaryList
import com.numero.itube.repository.PlaylistRepository
import javax.inject.Inject

class TopViewModel @Inject constructor(
        private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val actionLiveData: MutableLiveData<Action<EmptyAction>> = MutableLiveData()
    val playlistSummaryListLiveData: LiveData<PlaylistSummaryList> = actionLiveData.switchMap {
        playlistRepository.readPlaylistSummaryList()
    }

    fun executeLoadAllPlaylist() {
        actionLiveData.value = Action(EmptyAction)
    }

    private val createLiveData: MutableLiveData<Action<CreatePlaylistAction>> = MutableLiveData()
    val createPlaylistListLiveData: LiveData<Playlist> = createLiveData.switchMap {
        playlistRepository.createPlaylist(Playlist.createPlaylist(it.value.title))
    }

    fun executeCreatePlaylist(title: String) {
        createLiveData.value = Action(CreatePlaylistAction(title))
    }

    private data class CreatePlaylistAction(val title: String)
}