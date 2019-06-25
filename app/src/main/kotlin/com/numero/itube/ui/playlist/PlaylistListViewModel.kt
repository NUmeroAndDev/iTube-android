package com.numero.itube.ui.playlist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.numero.itube.model.Action
import com.numero.itube.model.EmptyAction
import com.numero.itube.model.Playlist
import com.numero.itube.model.PlaylistList
import com.numero.itube.repository.PlaylistRepository
import javax.inject.Inject

class PlaylistListViewModel @Inject constructor(
        private val playlistRepository: PlaylistRepository
) : ViewModel() {

    private val actionLiveData: MutableLiveData<Action<EmptyAction>> = MutableLiveData()
    val playlistListLiveData: LiveData<PlaylistList> = actionLiveData.switchMap {
        playlistRepository.readPlaylistList()
    }

    fun executeLoadPlaylist() {
        actionLiveData.value = Action(EmptyAction)
    }


    /**
     * FIXME 確認用
     */
    private val createLiveData: MutableLiveData<Action<CreatePlaylistAction>> = MutableLiveData()
    val createPlaylistListLiveData: LiveData<Playlist> = createLiveData.switchMap {
        playlistRepository.createPlaylist(Playlist.createPlaylist(it.value.title))
    }

    fun executeCreatePlaylist() {
        createLiveData.value = Action(CreatePlaylistAction("Hoge"))
    }

    private data class CreatePlaylistAction(
            val title: String
    )
}