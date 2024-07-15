package ru.netology.singlealbum.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.map
import ru.netology.singlealbum.dto.Album
import ru.netology.singlealbum.dto.Track
import ru.netology.singlealbum.model.AlbumModel
import ru.netology.singlealbum.repository.AlbumCallback
import ru.netology.singlealbum.repository.AlbumRepository


class AlbumViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = AlbumRepository()
    private val _data = MutableLiveData<AlbumModel>()

    init {
        loadAlbum()
    }

    val data: LiveData<AlbumModel>
        get() = _data

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

    fun setTrackInfo(newTrack: Track) {
        val album = _data.value?.album?.copy()
        val tracks = album?.tracks?.let {
            it.map { if (it.id == newTrack.id) newTrack.copy() else it.copy() }
        }
        _data.value = _data.value?.copy(album = album?.copy(tracks = tracks ?: emptyList()))
    }

}
