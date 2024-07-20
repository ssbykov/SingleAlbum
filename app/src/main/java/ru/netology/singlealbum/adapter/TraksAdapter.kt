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
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TraksAdapter @Inject constructor(private val items: MutableList<Track>) :
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

    fun updateItem(position: Int, track: Track) {
        items[position] = track
        notifyItemChanged(position)
    }

}

class TrackVieweHolder @Inject constructor(
    private val binding: SongCardBinding,
) : RecyclerView.ViewHolder(binding.root) {
    lateinit var mediaPlayerController: MediaPlayerController

    @SuppressLint("ClickableViewAccessibility")
    fun bind(
        track: Track,
    ) {

        with(binding) {
            trackName.text =
                binding.root.resources.getString(
                    R.string.track,
                    track.number.toString(),
                    track.file
                )
            progress.isEnabled = track.isPlaying

            playTrack.setOnCheckedChangeListener { isPressed ->
                val trackVieweHolderIntefaceImpl = TrackVieweHolderIntefaceImpl(binding)
                mediaPlayerController = trackVieweHolderIntefaceImpl.mediaPlayerController
                if (isPressed) {
                    if (progress.currentPosition == 0) {
                        mediaPlayerController.playTrack(track.file)
                    } else {
                        mediaPlayerController.pauseOff()
                    }
                } else {
                    mediaPlayerController.pauseOn()
                }
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