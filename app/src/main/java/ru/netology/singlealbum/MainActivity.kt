package ru.netology.singlealbum

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import ru.netology.singlealbum.adapter.TraksAdapter
import ru.netology.singlealbum.databinding.ActivityMainBinding
import ru.netology.singlealbum.dto.Album
import ru.netology.singlealbum.model.AlbumModel
import ru.netology.singlealbum.observer.MediaLifecycleObserver
import ru.netology.singlealbum.viewmodel.AlbumViewModel

class MainActivity : AppCompatActivity() {

    private val viewModel: AlbumViewModel by viewModels()
    private val observer = MediaLifecycleObserver()
    lateinit var adapter: TraksAdapter
    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycle.addObserver(observer)


        viewModel.data.observe(this, Observer { data ->
            updateUI(data)
        })

    }

    private fun updateUI(data: AlbumModel) {
        val tracks= data.album?.tracks ?: emptyList()
        adapter = TraksAdapter(tracks)
        binding.listItem.adapter = adapter
        adapter.submitList(data.album?.tracks)
        binding.album.text = data.album?.title
        binding.artist.text = data.album?.artist


    }
}
