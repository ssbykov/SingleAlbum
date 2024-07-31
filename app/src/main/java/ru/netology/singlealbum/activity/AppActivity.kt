package ru.netology.singlealbum.activity

import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.singlealbum.R
import ru.netology.singlealbum.adapter.TraksAdapter
import ru.netology.singlealbum.controller.MediaPlayerController
import ru.netology.singlealbum.controller.PlayMode
import ru.netology.singlealbum.databinding.ActivityMainBinding
import ru.netology.singlealbum.dto.Album
import ru.netology.singlealbum.viewmodel.AlbumViewModel

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

        viewModel.albumData.observe(this, Observer { data ->
            if (data.error) {
                Snackbar.make(
                    binding.root,
                    "Ошибка загрузки",
                    Snackbar.LENGTH_INDEFINITE
                )
                    .setAction("Повторить") {
                        viewModel.loadAlbum()
                    }.show()
            }
            binding.play.isEnabled = !data.load
            binding.progress.visibility = if (data.load) VISIBLE else GONE
            updateUI(data.album)
        })

        viewModel.tracksData.observe(this, Observer {
            adapter.submitList(it)
        })

        viewModel.isPlaying.observe(this, Observer {
            binding.play.setImageResource(
                if (it) R.drawable.ic_stop_24 else R.drawable.ic_play_arrow_24
            )
        })
        adapter = TraksAdapter(mediaPlayerController)
        binding.listItem.adapter = adapter


        binding.listItem.itemAnimator = null
    }

    private fun updateUI(album: Album?) {
        if (album == null) return
        binding.apply {
            albumName.text = album.title
            artist.text = album.artist
            information.text = getString(
                R.string.info,
                album.published,
                album.genre
            )
//            if (mediaPlayerController.isPlaying()) {
//                play.setImageResource(
//                    R.drawable.ic_stop_24
//                )
//            }
            play.setOnClickListener {
                if (mediaPlayerController.isPlaying()) {
                    viewModel.setIsPlaying(false)
                    mediaPlayerController.setPlayMode(null)
                    mediaPlayerController.stopCurrentTrack()
                } else {
                    val tracks = viewModel.tracksData.value
                    if (!tracks.isNullOrEmpty()) {
                        mediaPlayerController.setPlayMode(PlayMode.ALL)
                        mediaPlayerController.playTrack(tracks.first())
                    }
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
