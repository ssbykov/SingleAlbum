package ru.netology.singlealbum

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import ru.netology.singlealbum.adapter.TraksAdapter
import ru.netology.singlealbum.databinding.ActivityMainBinding
import ru.netology.singlealbum.dto.Album
import ru.netology.singlealbum.viewmodel.AlbumViewModel
import ru.netology.singlealbum.viewmodel.PlayMode

class MainActivity : AppCompatActivity() {

    private val viewModel: AlbumViewModel by viewModels()
    lateinit var adapter: TraksAdapter
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.data.observe(this, Observer { data ->
            updateUI(data.album)
        })

        adapter = TraksAdapter(viewModel)
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
                album.published ?: "",
                album.genre ?: ""
            )
            play.setImageResource(
                if (isPlaying) R.drawable.ic_stop_24 else R.drawable.ic_play_arrow_24
            )
            play.setOnClickListener {
                if (isPlaying) {
                    viewModel.stop()
                } else {
                    viewModel.play(tracks.first(), PlayMode.ALL)
                }
            }
            next.setOnClickListener {
                viewModel.playNext(1)
            }

            previous.setOnClickListener {
                viewModel.playNext(-1)
            }
        }
    }
}
