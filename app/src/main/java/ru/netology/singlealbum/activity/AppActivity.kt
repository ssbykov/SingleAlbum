package ru.netology.singlealbum.activity

import android.content.Context
import android.os.Bundle
import android.text.style.TtsSpan.CardinalBuilder
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.children
import androidx.core.view.get
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.singlealbum.R
import ru.netology.singlealbum.adapter.TraksAdapter
import ru.netology.singlealbum.controller.MediaPlayerController
import ru.netology.singlealbum.databinding.ActivityMainBinding
import ru.netology.singlealbum.databinding.SongCardBinding
import ru.netology.singlealbum.dto.Album
import ru.netology.singlealbum.model.TrackModel
import ru.netology.singlealbum.observer.MediaLifecycleObserver
import ru.netology.singlealbum.viewmodel.AlbumViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {

    private val viewModel: AlbumViewModel by viewModels()
    private val observer = MediaLifecycleObserver()
    lateinit var binding: ActivityMainBinding

    @Inject
    lateinit var adapter: TraksAdapter

    @Inject
    lateinit var mediaPlayerController: MediaPlayerController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycle.addObserver(observer)


        viewModel.data.observe(this, Observer { data ->
            updateUI(data.album)
        })
    }

    private fun updateUI(album: Album?) {

        album?.let {
            binding.apply {
                albumName.text = album.title
                artist.text = album.artist
                information.text = getString(
                    R.string.info,
                    album.published ?: "",
                    album.genre
                )
                play.setIcons(R.drawable.ic_play_arrow_24, R.drawable.ic_stop_24)

                play.setOnCheckedChangeListener { isPressed ->
                    if (isPressed) {
                        mediaPlayerController.play(adapter.data)
                    } else {
                        mediaPlayerController.play(emptyList())
                    }
                }

                mediaPlayerController.setOnFirstListener { isFirst ->
                    previous.background = AppCompatResources.getDrawable(
                        binding.root.context,
                        if (isFirst) R.drawable.roundcorner_gray else R.drawable.roundcorner_yellow
                    )
                    previous.isEnabled = !isFirst
                }

                mediaPlayerController.setOnLastListener { isLast ->
                    next.background = AppCompatResources.getDrawable(
                        binding.root.context,
                        if (isLast) R.drawable.roundcorner_gray else R.drawable.roundcorner_yellow
                    )
                    next.isEnabled = !isLast
                }

                mediaPlayerController.setOnPlayListFinishedListener { isLast ->
                    next.background = AppCompatResources.getDrawable(
                        binding.root.context,
                        R.drawable.roundcorner_gray
                    )
                    previous.background = AppCompatResources.getDrawable(
                        binding.root.context,
                        R.drawable.roundcorner_gray
                    )
                    play.setChecked(!isLast)
                }

                previous.setOnClickListener {
                    mediaPlayerController.playNext(-1)
                }

                next.setOnClickListener {
                    mediaPlayerController.playNext()
                }

                adapter.addItem(album.tracks, layoutInflater)

                adapter.data.forEach { trackModel ->
                    listItem.addView(trackModel.card.root)
                }
            }
        }
    }
}
