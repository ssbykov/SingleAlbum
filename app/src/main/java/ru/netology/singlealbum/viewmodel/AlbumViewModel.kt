package ru.netology.singlealbum.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

    private val _isPaused = MutableLiveData<Boolean>(true)
    val isPaused: LiveData<Boolean>
        get() = _isPaused

    private val _isAll = MutableLiveData<Boolean>()
    val isAll: LiveData<Boolean>
        get() = _isAll

    init {
        loadAlbum()
    }


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

    fun updateIsPaused(value: Boolean) {
        _isPaused.value = value
    }

    fun updateIsAll(value: Boolean) {
        _isAll.value = value
    }

}
