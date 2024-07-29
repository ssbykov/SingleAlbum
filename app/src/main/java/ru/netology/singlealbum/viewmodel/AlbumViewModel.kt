package ru.netology.singlealbum.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.netology.singlealbum.controller.MediaPlayerController
import ru.netology.singlealbum.dto.Album
import ru.netology.singlealbum.dto.Track
import ru.netology.singlealbum.model.AlbumModel
import ru.netology.singlealbum.repository.AlbumCallback
import ru.netology.singlealbum.repository.AlbumRepository


class AlbumViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AlbumRepository()

    private val _data = MutableLiveData<AlbumModel>()
    val data: LiveData<AlbumModel>
        get() = _data

    private val _isAll = MutableLiveData<Boolean>()
    val isAll: LiveData<Boolean>
        get() = _isAll

    private var playMode: PlayMode = PlayMode.ALL

    init {
        loadAlbum()
    }

    private val mediaPlayerController = MediaPlayerController(this)

    fun loadAlbum() {
        _data.postValue(AlbumModel(load = true))
        repository.getAlbum(object : AlbumCallback<Album> {
            override fun onSuccess(result: Album) {
                _data.postValue(AlbumModel(album = result))
            }

            override fun onError(e: Exception) {
                AlbumModel(error = true)
            }
        })
    }

    fun updateItem(newTrack: Track) {
        val newAlbum = _data.value?.album?.let {
            it.copy(
                tracks = it.tracks.map { track ->
                    if (track.id == newTrack.id) newTrack else track
                }.toMutableList()
            )
        }
        _data.value = _data.value?.copy(album = newAlbum)

    }

    fun play(track: Track, playMode: PlayMode) {
        this.playMode = playMode
        mediaPlayerController.playTrack(track, playMode)
    }

    fun playNext(track: Track) {
        mediaPlayerController.playTrack(track, playMode)
    }

    fun playNext(step: Int) {
        mediaPlayerController.nextTrack(playMode, step)
    }

    fun stop() {
        mediaPlayerController.stopCurrentTrack()
    }

    fun isPaused(): Boolean {
        return mediaPlayerController.isPaused() ?: false
    }

    fun isPlaying(): Boolean {
        return mediaPlayerController.isPlaying()
    }

    fun setPlayPause() {
        val isPaused = mediaPlayerController.isPaused()
        if (isPaused == null) return
        if (isPaused) mediaPlayerController.pauseOff()
        else mediaPlayerController.pauseOn()
    }

    fun updateProgress(progress: Int) {
        mediaPlayerController.updateProgress(progress ?: 0)
    }
}

enum class PlayMode {
    SINGLE, ALL
}
