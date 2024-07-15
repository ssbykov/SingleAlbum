package ru.netology.singlealbum

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import ru.netology.singlealbum.adapter.TraksAdapter
import ru.netology.singlealbum.controller.MediaPlayerController
import ru.netology.singlealbum.controller.MediaPlayerListener
import ru.netology.singlealbum.databinding.ActivityMainBinding
import ru.netology.singlealbum.dto.Track
import ru.netology.singlealbum.observer.MediaLifecycleObserver
import ru.netology.singlealbum.utils.fromMillis
import ru.netology.singlealbum.viewmodel.AlbumViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: AlbumViewModel by viewModels()
    private val observer = MediaLifecycleObserver()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycle.addObserver(observer)

        val playInterface = object : PlayInterface {
            override fun playTrack(track: Track) {
                play(track)
            }

        }

        val mediaPlayerController = MediaPlayerController()
        val adapter = TraksAdapter(mediaPlayerController)
        binding.listItem.adapter = adapter

        viewModel.data.observe(this) {
            adapter.submitList(it.album?.tracks)
            binding.album.text = it.album?.title
            binding.artist.text = it.album?.artist
        }

    }

    fun play(track: Track): Unit {
        observer.player?.apply {
            reset()
            setDataSource(
                "https://raw.githubusercontent.com/netology-code/andad-homeworks/master/09_multimedia/data/" + track.file
            )
            prepare()
            val handler = Handler(Looper.getMainLooper())
            val runnable = object : Runnable {
                override fun run() {
                    val newTrack = track.copy(duration = duration, played = currentPosition)
                    viewModel.setTrackInfo(newTrack)
                    handler.postDelayed(this, 1000) // Обновление каждую секунду
                }
            }
            handler.post(runnable)
        }
        observer.player?.start()
    }
}

interface PlayInterface {
    fun playTrack(track: Track)
}