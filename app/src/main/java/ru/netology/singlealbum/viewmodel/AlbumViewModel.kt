package ru.netology.singlealbum.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.netology.singlealbum.dto.Album
import ru.netology.singlealbum.dto.Track
import ru.netology.singlealbum.model.AlbumModel
import ru.netology.singlealbum.repository.AlbumCallback
import ru.netology.singlealbum.repository.AlbumRepository
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepository,
    application: Application,
) : AndroidViewModel(application) {


    private val _albumData = MutableLiveData<AlbumModel>()
    val albumData: LiveData<AlbumModel>
        get() = _albumData

    private val _tracksData = MutableLiveData<List<Track>>()
    val tracksData: LiveData<List<Track>>
        get() = _tracksData

    private val _isPlaying = MutableLiveData<Boolean>()
    val isPlaying: LiveData<Boolean>
        get() = _isPlaying


    init {
        loadAlbum()
    }

    fun loadAlbum() {
        _albumData.postValue(AlbumModel(load = true))
        repository.getAlbum(object : AlbumCallback<Album> {
            override fun onSuccess(result: Album) {
                _albumData.postValue(AlbumModel(album = result))
                _tracksData.postValue(result.tracks)
            }

            override fun onError(e: Exception) {
                AlbumModel(error = true)
            }
        })
    }

    fun updateItem(newTrack: Track) {
        _tracksData.value = _tracksData.value?.let { tracks ->
            tracks.map { it ->
                if (it.id == newTrack.id) newTrack else it
            }.toMutableList()
        }
    }

    fun setIsPlaying(state: Boolean) {
        _isPlaying.postValue(state)
    }
}
