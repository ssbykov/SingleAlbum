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

class AlbumViewModel @Inject constructor(application: Application) : AndroidViewModel(application) {
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
                var number = 0
                val tracks = result.tracks.map {
                    it.copy(number = ++number)
                }
                val album = result.copy(tracks = tracks)
                _data.postValue(AlbumModel(album = album))
            }

            override fun onError(e: Exception) {
                _data.postValue(AlbumModel(error = true))
            }
        })
    }

}
