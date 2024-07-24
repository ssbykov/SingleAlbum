package ru.netology.singlealbum.activity

import android.content.Context
import android.os.Bundle
import android.text.style.TtsSpan.CardinalBuilder
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.children
import androidx.core.view.get
import androidx.lifecycle.Observer
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.singlealbum.R
import ru.netology.singlealbum.adapter.TraksAdapter
import ru.netology.singlealbum.controller.MediaPlayerController
import ru.netology.singlealbum.databinding.ActivityMainBinding
import ru.netology.singlealbum.databinding.SongCardBinding
import ru.netology.singlealbum.dto.Album
import ru.netology.singlealbum.model.TrackModel
import ru.netology.singlealbum.observer.MediaLifecycleObserver
import ru.netology.singlealbum.utils.setColorState
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
                replay.setIcons(R.drawable.ic_loop_24, R.drawable.ic_arrow_forward_24)
                previous.isEnabled = false
                next.isEnabled = false

                replay.setOnCheckedChangeListener { isPressed ->
                    mediaPlayerController.setPlayMode(!isPressed)
                    if (!mediaPlayerController.isEmptyPlayList()) {
                        setColorState(next, isPressed && mediaPlayerController.isLast())
                        setColorState(previous, isPressed && mediaPlayerController.isFirst())
                    }
                }

                play.setOnCheckedChangeListener { isPressed ->
                    mediaPlayerController.play(if (isPressed) adapter.data else emptyList())
                }

                mediaPlayerController.setOnFirstListener { isFirst ->
                    setColorState(previous, isFirst)
                }

                mediaPlayerController.setOnLastListener { isLast ->
                    setColorState(next, isLast)
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
