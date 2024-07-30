package ru.netology.singlealbum.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.netology.singlealbum.dto.Album
import ru.netology.singlealbum.dto.Track
import ru.netology.singlealbum.model.AlbumModel
import ru.netology.singlealbum.repository.AlbumCallback
import ru.netology.singlealbum.repository.AlbumRepository
import javax.inject.Inject

@HiltViewModel
class AlbumViewModel @Inject constructor(
    private val repository: AlbumRepository
) : ViewModel() {


    private val _data = MutableLiveData<AlbumModel>()
    val data: LiveData<AlbumModel>
        get() = _data


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
}
