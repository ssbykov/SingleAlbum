package ru.netology.singlealbum.activity

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.singlealbum.R
import ru.netology.singlealbum.adapter.TraksAdapter
import ru.netology.singlealbum.controller.MediaPlayerController
import ru.netology.singlealbum.databinding.ActivityMainBinding
import ru.netology.singlealbum.dto.Album
import ru.netology.singlealbum.observer.MediaLifecycleObserver
import ru.netology.singlealbum.viewmodel.AlbumViewModel
import javax.inject.Inject

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {

    private val viewModel: AlbumViewModel by viewModels()
    private val observer = MediaLifecycleObserver()
    lateinit var binding: ActivityMainBinding
    private lateinit var container: LinearLayout
    @Inject
    lateinit var adapter: TraksAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycle.addObserver(observer)

        container = binding.listItem



        viewModel.data.observe(this, Observer { data ->
            updateUI(data.album)
        })
    }

    private fun updateUI(album: Album?) {
        binding.apply {
            adapter.addItem(album?.tracks, container)
            MediaPlayerController.initialize(adapter)
            albumName.text = album?.title
            artist.text = album?.artist
            information.text = getString(
                R.string.info,
                album?.published ?: "",
                album?.genre ?: ""
            )
        }
    }

}
