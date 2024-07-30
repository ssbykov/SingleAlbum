package ru.netology.singlealbum.activity

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.singlealbum.R
import ru.netology.singlealbum.adapter.TraksAdapter
import ru.netology.singlealbum.controller.MediaPlayerController
import ru.netology.singlealbum.controller.PlayMode
import ru.netology.singlealbum.databinding.ActivityMainBinding
import ru.netology.singlealbum.dto.Album
import ru.netology.singlealbum.viewmodel.AlbumViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {

    lateinit var adapter: TraksAdapter
    lateinit var binding: ActivityMainBinding

    private val viewModel: AlbumViewModel by viewModels()

    lateinit var mediaPlayerController: MediaPlayerController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mediaPlayerController = MediaPlayerController.getInstance(viewModel)

        mediaPlayerController.viewModel.data.observe(this, Observer { data ->
            updateUI(data.album)
        })

        adapter = TraksAdapter(mediaPlayerController)
        binding.listItem.adapter = adapter

        binding.listItem.itemAnimator = null
    }

    private fun updateUI(album: Album?) {
        val tracks = album?.tracks
        if (tracks == null) return
        val isPlaying = tracks.any { it.isPlaying }
        binding.apply {
            adapter.submitList(album.tracks)
            albumName.text = album.title
            artist.text = album.artist
            information.text = getString(
                R.string.info,
                album.published,
                album.genre
            )
            play.setImageResource(
                if (isPlaying) R.drawable.ic_stop_24 else R.drawable.ic_play_arrow_24
            )
            play.setOnClickListener {
                if (isPlaying) {
                    mediaPlayerController.stopCurrentTrack()
                } else {
                    mediaPlayerController.setPlayMode(PlayMode.ALL)
                    mediaPlayerController.playTrack(tracks.first())
                }
            }
            next.setOnClickListener {
                mediaPlayerController.nextTrack(1)
            }

            previous.setOnClickListener {
                mediaPlayerController.nextTrack(-1)
            }
        }
    }
}
