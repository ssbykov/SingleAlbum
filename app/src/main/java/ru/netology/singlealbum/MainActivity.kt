package ru.netology.singlealbum

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import ru.netology.singlealbum.adapter.TrackVieweHolder
import ru.netology.singlealbum.adapter.TraksAdapter
import ru.netology.singlealbum.controller.MediaPlayerController
import ru.netology.singlealbum.databinding.ActivityMainBinding
import ru.netology.singlealbum.databinding.SongCardBinding
import ru.netology.singlealbum.dto.Album
import ru.netology.singlealbum.model.AlbumModel
import ru.netology.singlealbum.observer.MediaLifecycleObserver
import ru.netology.singlealbum.viewmodel.AlbumViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: AlbumViewModel by viewModels()
    lateinit var mediaPlayerController: MediaPlayerController
    private val observer = MediaLifecycleObserver()
    lateinit var adapter: TraksAdapter
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycle.addObserver(observer)

        mediaPlayerController = MediaPlayerController.getInstance(viewModel)

        viewModel.data.observe(this, Observer { data ->
            updateUI(data.album)
        })

    }

    private fun updateUI(album: Album?) {
        val tracks = album?.tracks
        if (tracks == null) return
        val isPaused = viewModel.isPaused.value ?: true
        adapter = TraksAdapter(tracks, isPaused, mediaPlayerController)
        binding.apply {
            listItem.adapter = adapter
            adapter.submitList(album.tracks)
            albumName.text = album.title
            artist.text = album.artist
            information.text = getString(
                R.string.info,
                album.published ?: "",
                album.genre ?: ""
            )
            play.setImageResource(
                if (!isPaused) R.drawable.ic_pause_24 else R.drawable.ic_play_arrow_24
            )
        }
    }
}
