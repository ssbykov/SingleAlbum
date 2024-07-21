package ru.netology.singlealbum.activity

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import ru.netology.singlealbum.adapter.TraksAdapterNew
import ru.netology.singlealbum.databinding.ActivityMainBinding
import ru.netology.singlealbum.observer.MediaLifecycleObserver
import ru.netology.singlealbum.viewmodel.AlbumViewModel

@AndroidEntryPoint
class AppActivity : AppCompatActivity() {

    private val viewModel: AlbumViewModel by viewModels()
    private val observer = MediaLifecycleObserver()
    lateinit var binding: ActivityMainBinding
    private lateinit var container: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycle.addObserver(observer)

        container = binding.listItem

        val adapter = TraksAdapterNew

        viewModel.data.observe(this, Observer { data ->
            adapter.addItem(data.album?.tracks, container)
        })

        adapter.cards
    }


}
