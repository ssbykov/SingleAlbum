package ru.netology.singlealbum.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import ru.netology.singlealbum.R
import ru.netology.singlealbum.controller.MediaPlayerController
import ru.netology.singlealbum.databinding.SongCardBinding
import ru.netology.singlealbum.dto.Track
import ru.netology.singlealbum.utils.fromMillis

class TraksAdapter(private val items: List<Track>) :
    ListAdapter<Track, TrackVieweHolder>(TrackDiffCallback) {
    override fun onBindViewHolder(holder: TrackVieweHolder, position: Int) {
        val track = getItem(position)
        holder.bind(track)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackVieweHolder {
        val binding = SongCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TrackVieweHolder(binding)
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getItemId(position: Int): Long {
        return items[position].id.toLong()
    }

}

class TrackVieweHolder(
    private val binding: SongCardBinding,
) : RecyclerView.ViewHolder(binding.root) {
    lateinit var mediaPlayerController: MediaPlayerController
    fun bind(
        track: Track,
    ) {

        with(binding) {
            trackName.text = track.file
            progress.isEnabled = false
            playTrack.setOnClickListener {
                val trackVieweHolderInteface = object : TrackVieweHolderInteface {

                    override fun setNewCard(): SongCardBinding {
                        return binding
                    }

                    override fun initNewCard(
                        newCard: SongCardBinding?,
                        currentPosition: Int,
                        duration: Int
                    ) {
                        newCard?.let {
                            it.progress.progress = (currentPosition * 100) / duration
                            it.playTrack.isPressed = true
                            it.progress.isEnabled = true
                            val zero = it.root.resources.getString(R.string._0_00)
                            if (it.time.text == zero) {
                                it.time.setText(fromMillis(duration))
                            }
                        }
                    }

                    override fun resetCongCard(oldSongCard: SongCardBinding?) {
                        oldSongCard?.let {
                            it.progress.progress = 0
                            it.playTrack.isPressed = false
                            it.progress.isEnabled = false
                        }
                    }

                }
                mediaPlayerController = MediaPlayerController.getInstance(trackVieweHolderInteface)
                mediaPlayerController.playTrack(track.file, binding)
            }
            progress.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                var setProgress: Int? = null
                override fun onProgressChanged(
                    seekBar: SeekBar?,
                    progress: Int,
                    fromUser: Boolean
                ) {
                    setProgress = progress
                }

                override fun onStartTrackingTouch(seekBar: SeekBar?) {}

                override fun onStopTrackingTouch(seekBar: SeekBar?) {
                    mediaPlayerController.updateProgress(setProgress ?: 0)
                }

            })
        }
    }
}

object TrackDiffCallback : DiffUtil.ItemCallback<Track>() {
    override fun areContentsTheSame(oldItem: Track, newItem: Track) =
        oldItem.id == newItem.id

    override fun areItemsTheSame(oldItem: Track, newItem: Track) =
        oldItem == newItem

}

interface TrackVieweHolderInteface {
    fun setNewCard(): SongCardBinding
    fun initNewCard(newCard: SongCardBinding?, currentPosition: Int, duration: Int)
    fun resetCongCard(oldSongCard: SongCardBinding?)
}