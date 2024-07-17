package ru.netology.singlealbum.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.content.ContextCompat
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

    @SuppressLint("ClickableViewAccessibility")
    fun bind(
        track: Track,
    ) {

        with(binding) {
            trackName.text = track.file
            progress.isEnabled = false

            playTrack.setOnTouchListener { _, event ->
                val trackVieweHolderIntefaceImpl = TrackVieweHolderIntefaceImpl(binding)

                mediaPlayerController = trackVieweHolderIntefaceImpl.mediaPlayerController

                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        playTrack.isPressed = !playTrack.isPressed
                        true
                    }

                    MotionEvent.ACTION_UP -> {
                        if (playTrack.isPressed) {
                            if (progress.progress == 0) {
                                mediaPlayerController.playTrack(track.file)
                            } else {
                                mediaPlayerController.pauseOff()
                            }
                        } else {
                            mediaPlayerController.pauseOn()
                        }
                        true
                    }

                    else -> false
                }

                playTrack.isPressed
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